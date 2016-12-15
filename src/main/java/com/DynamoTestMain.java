package com;

import com.geotest.clients.DymanoManager;
import com.geotest.entities.Location;

import java.io.IOException;

/**
 * Created by cryptobat on 12/15/2016.
 */
public class DynamoTestMain {
  public static void main(String[] args) {
  }

  public void fill(){
    DymanoManager dymanoManager = new DymanoManager();
    dymanoManager.fillDatabase();
  }

}
