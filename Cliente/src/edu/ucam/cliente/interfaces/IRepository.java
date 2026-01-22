package edu.ucam.cliente.interfaces;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import edu.ucam.domain.Titulacion;

public interface IRepository <T>{
	public String add(String idObjeto, T modelo) throws ClassNotFoundException;
	public String delete(String idObjeto) throws IOException;
	public List<T> list()throws IOException, ClassNotFoundException;
	public String update(String idObjeto, T modelo) throws IOException, ClassNotFoundException;
	public T getModel(String idObjeto) throws IOException, ClassNotFoundException;
	public int modelSize();
	public T crearObjeto(Scanner S, String idObjeto);
	public T crearObjeto(String idObjeto);
}
