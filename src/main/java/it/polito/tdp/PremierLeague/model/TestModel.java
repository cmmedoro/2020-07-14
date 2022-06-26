package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Model m = new Model();
		for(Team t : m.getAllTeams()) {
			System.out.println(t.getName());
		}
		m.getClassifica();

	}

}
