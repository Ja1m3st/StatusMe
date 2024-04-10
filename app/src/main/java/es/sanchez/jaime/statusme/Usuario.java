package es.sanchez.jaime.statusme;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private int id;
    private String name;
    private String lastname; // Make a Split for separate the name to the lastname
    private String mail; // Make a control for caracters
    private String password; // Make a system that controls the caracters

    private Estados mi_lista_de_estados;

    public Usuario(String name, String lastname, String mail, String password, Estados lista_estado ){
        this.name = name;
        this.lastname = lastname;
        this.mail = mail;
        this.password = password;
        this.mi_lista_de_estados = lista_estado;
    }
    public static class UsuarioGoogle extends Usuario {
        public UsuarioGoogle(String name, String lastname, String mail) {
            super(name, lastname, mail, null,null);
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

    public Estados getMi_lista_de_estados() {
        return mi_lista_de_estados;
    }

    public void setMi_lista_de_estados(Estados mi_lista_de_estados) {
        this.mi_lista_de_estados = mi_lista_de_estados;
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
