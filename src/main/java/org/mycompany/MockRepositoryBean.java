package org.mycompany;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import io.swagger.client.model.Moneda;

@Component
public class MockRepositoryBean {

	private AtomicInteger cdor = new AtomicInteger(0);
	
	private List<Moneda> list = new ArrayList<Moneda>();
	
	public List<Moneda> list(){
		return list;
	}
	
	public void create(Moneda entity) {
		entity.setId(cdor.incrementAndGet());
		list.add(entity);
	}
	
	public Moneda get(Integer id) {
		Moneda entity = null;
		for(Moneda item : list){
			if(id.equals(item.getId())){
				entity = item;
			}
		}
		return entity;
	}
	
	public void update(Integer id, Moneda entity) {
		Moneda item = get(id);
		item.setSimbolo(entity.getSimbolo());
		item.setDescripcion(entity.getDescripcion());
		item.setCotizacion(entity.getCotizacion());
	}

	public void remove(Integer id) {
		Moneda entity = null;
		for(Moneda item : list){
			if(id.equals(item.getId())){
				entity = item;
			}
		}
		
		if(entity != null) {
			list.remove(entity);
		}
		
	}
	
}