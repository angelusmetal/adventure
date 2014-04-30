package com.adventure.example;

import java.util.Collections;

import com.adventure.engine.core.Context;
import com.adventure.engine.core.Location;
import com.adventure.engine.core.LocationConnection;

public class ExampleRunner {

	public static void main(String[] args) {
		
		Location pradera = new Location() {};
		pradera.setName("pradera");
		
		Location castillito = new Location() {};
		castillito.setName("castillito");
		
		Context context = new Context();
		context.setCurrentLocation(pradera);
		
//		pradera.addLocation(new LocationConnection(pradera, castillito, Collections.singletonList("castillito"), Collections.singletonList("pardera")));
		
	}

}
