package com.example.demo.server;

import com.example.demo.model.Candidate;
import com.example.demo.model.Profile;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class VoteMachine extends BasicServer {
    private CandidatesDataModel candidatesDataModel = new CandidatesDataModel();
    private ProfileDataModel profileDataModel = new ProfileDataModel();

    protected VoteMachine(String host, int port) throws IOException {
        super(host, port);

        registerGet("/", this::mainHandler);
        registerPost("/vote", this::voteHandler);
        registerGet("/login", this::loginGetHandler);
        registerPost("/login", this::loginPostHandler);
        registerGet("/thankyou", this::thanksHandler);
        registerGet("votes", this::totalVotesHandler);
    }

    private void totalVotesHandler(HttpExchange exchange) {
        candidatesDataModel.getCandidates().sort(Comparator.comparingInt(Candidate::getVotes).reversed());
        Candidate topCandidate = candidatesDataModel.getCandidates().get(0); // Установка победителя
        if (topCandidate.getVotes() != 0){
            topCandidate.setTop(true);// Установка победителя
        }
        candidatesDataModel.getCandidates().forEach(candidate -> {
            if (topCandidate.getVotes().equals(topCandidate.getVotes()) && candidate.getVotes()!= 0) {
                candidate.setTop(true);
            }
        });
        renderTemplate(exchange, "/votes.html", candidatesDataModel);
    }

    private void thanksHandler(HttpExchange exchange) {
        renderTemplate(exchange, "/thankyou.html", candidatesDataModel);
    }

    private void loginPostHandler(HttpExchange exchange) {
        var parsed = Utils.parseUrlEncoded(getBody(exchange), "&");
        String mail = parsed.getOrDefault("email", "");
        String password = parsed.getOrDefault("user-password", "");
        if (isProfileExist(mail, password)) {
            String sessionId = Utils.getSessionId();
            Cookie cookie = Cookie.make("sessionId", sessionId);
            cookie.setMaxAge(60 * 10);
            cookie.setHttpOnly(true);
            setCookies(exchange, cookie);
            profileDataModel.getProfiles().forEach(profile -> {
                if (profile.getEmail().equals(mail)) {
                    profileDataModel.setProfile(profile);
                    profile.setSessionId(sessionId);
                }
            });
            redirect303(exchange, "/");
        } else {
            redirect303(exchange, "/loginWithWrongEnter.html");
        }

    }

    private void loginGetHandler(HttpExchange exchange) {
        Path path = makeFilePath("login.html");
        sendFile(exchange, path, ContentType.TEXT_HTML);
    }

    private void voteHandler(HttpExchange exchange) {
        candidatesDataModel.getCandidates().sort(Comparator.comparingInt(Candidate::getId));
        var parsedBody = Utils.parseUrlEncoded(getBody(exchange), "&");
        int candidateId = Integer.parseInt(parsedBody.get("candidateId"));
        if (isLogIn(exchange)) {
            /*
            List Emails -> Все поголосовавщие
            Current profile текущий профиль. Он назначается после входа в систему в candidatesDataModel
             */
            Profile currentProfile = profileDataModel.getProfile();
            List<String> emails = candidatesDataModel.getEmailsOfVotedProfiles();
            if (emails.contains(currentProfile.getEmail())) { // если Содержиться маил в листе проголосовавщих
                redirect303(exchange, "alreadyVoted.html");
            } else {
                Candidate candidate = null;
                for (int i = 0; i < candidatesDataModel.getCandidates().size(); i++) {
                    Candidate currentCandidate = candidatesDataModel.getCandidates().get(i);
                    if (currentCandidate.getId() == candidateId) {
                        candidate = currentCandidate;
                        candidatesDataModel.setCurrentCandidate(currentCandidate);
                        currentCandidate.setVotes(currentCandidate.getVotes() + 1);
                        break;
                    }
                }
                if (candidate == null) {
                    redirect303(exchange, "/wrongIdOfCandidate.html");
                }
                candidatesDataModel.getEmailsOfVotedProfiles().add(currentProfile.getEmail());
                redirect303(exchange, "/thankyou");
            }
        } else {
            redirect303(exchange, "/login.html");
        }
    }

    private void mainHandler(HttpExchange exchange) {
        renderTemplate(exchange, "candidates.html", candidatesDataModel);
    }

    protected boolean isLogIn(HttpExchange exchange) {
        var parsed = Utils.parseUrlEncoded(getCookies(exchange), "; ");
        String sessionId = parsed.getOrDefault("sessionId", "");

        for (Profile profile : profileDataModel.getProfiles()) {
            if (sessionId.equals(profile.getSessionId())) {
                return true;
            }
        }
        return false;
    }

    private boolean isProfileExist(String email, String password) {
        for (Profile profile : profileDataModel.getProfiles()) {
            if (profile.getEmail().equals(email) && profile.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}