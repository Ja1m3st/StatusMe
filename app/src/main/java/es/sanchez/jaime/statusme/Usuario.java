package es.sanchez.jaime.statusme;

import java.util.ArrayList;

public class Usuario {
    private int id;
    private String name;
    private String lastname; // Make a Split for separate the name to the lastname
    private String mail; // Make a control for caracters
    private String password; // Make a system that controls the caracters
    private ArrayList<ArrayList> totaldias;

    public Usuario (){
    }

    public Usuario (String nombre, String apellido){
        this.name = nombre;
        this.lastname = apellido;
    }

    public Usuario(String name, String lastname, String mail, String password, ArrayList<ArrayList> totaldias) {
        this.name = name;
        this.lastname = lastname;
        this.mail = mail;
        this.password = password;
        this.totaldias = totaldias;
    }

    public static class UsuarioGoogle extends Usuario {
        public UsuarioGoogle(String name, String lastname, String mail, ArrayList<ArrayList> totaldias) {
            super(name, lastname, mail, null, totaldias);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<ArrayList> getTotaldias() {
        return totaldias;
    }

    public void setTotaldias(ArrayList<ArrayList> totaldias) {
        this.totaldias = totaldias;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
