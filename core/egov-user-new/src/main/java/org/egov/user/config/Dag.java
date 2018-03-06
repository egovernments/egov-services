package org.egov.user.config;

import java.util.ArrayList;
import java.util.List;

public class Dag {

	private int v;

	private ArrayList<Integer>[] adjList;

	public Dag(int vertices) {
		this.v = vertices;
		initAdjList();
	}

	@SuppressWarnings("unchecked")
	private void initAdjList() {
		adjList = new ArrayList[v];

		for (int i = 0; i < v; i++) {
			adjList[i] = new ArrayList<>();
		}
	}

	// add edge from u to v
	public void addEdge(int u, int v) {
		adjList[u].add(v);
	}

	public void printAllPaths(int s, int d) {
		boolean[] isVisited = new boolean[v];
		ArrayList<Integer> pathList = new ArrayList<>();
		pathList.add(s);
		printAllPathsUtil(s, d, isVisited, pathList);
	}

	private void printAllPathsUtil(Integer u, Integer d, boolean[] isVisited, List<Integer> localPathList) {
		isVisited[u] = true;

		if (u.equals(d)) {
			System.out.println(localPathList);
		}

		for (Integer i : adjList[u]) {
			if (!isVisited[i]) {
				localPathList.add(i);
				printAllPathsUtil(i, d, isVisited, localPathList);
				localPathList.remove(i);
			}
		}

		isVisited[u] = false;
	}

	public static void main(String[] args) {
		// Create a sample graph
		Dag g = new Dag(7);
		g.addEdge(0, 1);
		g.addEdge(0, 2);
		g.addEdge(1, 3);
		g.addEdge(1, 5);
		g.addEdge(2, 5);
		g.addEdge(6, 2);

		int s = 0;
		int d = 5;

		System.out.println("Following are all different paths from " + s + " to " + d);
		g.printAllPaths(s, d);

	}
}
