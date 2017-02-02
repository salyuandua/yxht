/*
 * ReadFile.java
 *
 * Created on 2007��1��19��, ����3:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.cheers.importData;

import java.util.Vector;
/**
 *
 * @author Administrator
 */
public interface ReadFile {
	public Vector getColumns(String fileName);
	public Vector getContent(String fileName);
}
