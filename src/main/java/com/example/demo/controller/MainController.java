package com.example.demo.controller;

import com.example.demo.model.Candidate;

import com.example.demo.server.CandidatesDataModel;

import com.sun.net.httpserver.HttpExchange;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Comparator;


@Controller
public class MainController {

    private final Candidate anon = new Candidate("anon.jpeg");


    @RequestMapping(value = "/")
    public String mainHandler(Model model) {
        CandidatesDataModel candidatesDataModel = new CandidatesDataModel();
        model.addAttribute("candidates", candidatesDataModel.getCandidates());
        return "candidates";
    }

    @RequestMapping(value = "/thankyou")
    public String thanksHandler(Integer candidateId, Model model) {
        CandidatesDataModel candidatesDataModel = new CandidatesDataModel();
        try {
            Candidate currentCandidate = candidatesDataModel.getCurrentCandidate(candidateId);
            model.addAttribute("candidate", currentCandidate);
        } catch (Exception e) {
            model.addAttribute("candidate", anon);
        }
        return "thankyou";
    }


    @RequestMapping(value = "/vote", method = RequestMethod.POST)
    public String voteHandler(Integer candidateId) {
        CandidatesDataModel candidatesDataModel = new CandidatesDataModel();
        try {
            Candidate currentCandidate = candidatesDataModel.getCurrentCandidate(candidateId);
            currentCandidate.setVotes(currentCandidate.getVotes() + 1);
            //добавляем голос
            return "redirect:/thankyou?candidateId=" + currentCandidate.getId();

        } catch (Exception ignored) {
        }
        return "redirect:/thankyou";
    }



    @RequestMapping(value = "/votes")
    public String totalVotesHandler(Model model) {
        CandidatesDataModel candidatesDataModel = new CandidatesDataModel();

        candidatesDataModel.getCandidates().sort(Comparator.comparingInt(Candidate::getVotes).reversed());
        Candidate topCandidate = candidatesDataModel.getCandidates().get(0); // Установка победителя
        if (topCandidate.getVotes() != 0) {
            topCandidate.setTop(true);// Установка победителя
        }
        candidatesDataModel.getCandidates().forEach(candidate -> {
            if (topCandidate.getVotes().equals(topCandidate.getVotes()) && candidate.getVotes() != 0) {
                candidate.setTop(true);
            }
        });

        model.addAttribute("topCandidate", topCandidate);
        return "votes";
    }

}






