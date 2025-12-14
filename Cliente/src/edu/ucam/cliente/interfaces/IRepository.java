package edu.ucam.cliente.interfaces;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public interface IRepository <T>{
	public void add(T modelo) throws IOException, ClassNotFoundException;
	public void delete() throws IOException;
	public List<T> list()throws IOException, ClassNotFoundException;
	public void update(String idObjeto, T modelo) throws IOException, ClassNotFoundException;
	public T getModel(String idObjeto) throws IOException, ClassNotFoundException;
	public String sendModel(String idObjeto, Object model) throws IOException;
	public int modelSize();
	public T crearObjeto(Scanner S, String idObjeto);
}
