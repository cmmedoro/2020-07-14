/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Statistiche;
import it.polito.tdp.PremierLeague.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnClassifica"
    private Button btnClassifica; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="cmbSquadra"
    private ComboBox<Team> cmbSquadra; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doClassifica(ActionEvent event) {
    	this.txtResult.clear();
    	//Controlla input
    	if(!this.model.isGraphCreated()) {
    		this.txtResult.setText("Devi prima creare il grafo");
    		return;
    	}
    	Team t = this.cmbSquadra.getValue();
    	if(t == null) {
    		this.txtResult.setText("Devi prima selezionare una squadra");
    		return;
    	}
    	//proseguo
    	List<Statistiche> migliori = this.model.squadreMigliori(t);
    	this.txtResult.appendText("SQUADRE MIGLIORI: \n");
    	for(Statistiche m : migliori) {
    		this.txtResult.appendText(m.toString()+"\n");
    	}
    	List<Statistiche> battute = this.model.squadreBattute(t);
    	this.txtResult.appendText("SQUADRE BATTUTE: \n");
    	for(Statistiche b : battute) {
    		this.txtResult.appendText(b.toString()+"\n");
    	}

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	// non ci sono controllo sull'input da fare in questo caso
    	this.model.creaGrafo();
    	this.txtResult.setText("Grafo creato\n");
    	this.txtResult.appendText("#VERTICI: "+this.model.nVertices()+"\n");
    	this.txtResult.appendText("#ARCHI: "+this.model.nArchi()+"\n");

    }

    @FXML
    void doSimula(ActionEvent event) {
    	this.txtResult.clear();
    	if(!this.model.isGraphCreated()) {
    		this.txtResult.setText("Devi prima creare il grafo");
    		return;
    	}
    	int N;
    	try {
    		N = Integer.parseInt(this.txtN.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Devi inserire un valore numerico intero per N");
    		return;
    	}
    	int X;
    	try {
    		X = Integer.parseInt(this.txtX.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Devi inserire un valore numerico intero per X");
    		return;
    	}
    	this.model.simula(N, X);
    	this.txtResult.appendText("Numero reporter che hanno in media assistito a una partita: "+this.model.getMedia()+"\n");
    	this.txtResult.appendText("Numero di partite sotto soglia: "+this.model.sottoSoglia()+"\n");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnClassifica != null : "fx:id=\"btnClassifica\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbSquadra != null : "fx:id=\"cmbSquadra\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbSquadra.getItems().clear();
    	this.cmbSquadra.getItems().addAll(this.model.getAllTeams());
    }
}
