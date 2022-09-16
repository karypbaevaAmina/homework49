//package com.example.homework50.service;
//
//import com.example.homework50.service.Candidate;
//
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class CandidatesDataModel {
//    private List<Candidate> candidates = new ArrayList<>();
//    private List<String> emailsOfVotedProfiles = new ArrayList<>();
//    private Candidate currentCandidate;
//
//    public CandidatesDataModel() {
//        try {
//            this.candidates = Utils.getCandidatesFromFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Integer countId = 1;
//        for (Candidate candidate : candidates) {
//            candidate.setId(countId);
//            countId++;
//        }
//    }
//
//    public List<String> getEmailsOfVotedProfiles() {
//        return emailsOfVotedProfiles;
//    }
//
//
//    public List<Candidate> getCandidates() {
//        return candidates;
//    }
//
//    public void setCandidates(List<Candidate> candidates) {
//        this.candidates = candidates;
//    }
//
//    public Candidate getCurrentCandidate() {
//        return currentCandidate;
//    }
//
//    public void setCurrentCandidate(Candidate currentCandidate) {
//        this.currentCandidate = currentCandidate;
//    }
//}