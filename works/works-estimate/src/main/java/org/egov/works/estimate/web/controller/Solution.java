package org.egov.works.estimate.web.controller;

import java.io.IOException;
import java.util.Scanner;

public class Solution {

	static String winner(int[] andrea, int[] maria, String s) {
		int andreaPoints = 0;
		int mariaPoints = 0;
		if ("odd".equalsIgnoreCase(s)) {
			for (int i = 1; (i % 2 != 0 && i < andrea.length); i++) {
				if (i % 2 == 0) {
					andreaPoints += andrea[i] - maria[i];
					mariaPoints += maria[i] - andrea[i];
				}
			}
		} else if ("even".equalsIgnoreCase(s)) {
			for (int i = 0; i < andrea.length; i++) {
				if (i % 2 == 0) {
					andreaPoints += andrea[i] - maria[i];
					mariaPoints += maria[i] - andrea[i];
				}
			}
		} else
			System.out.println("invalid code");

		// System.out.println("andreaPoints "+andreaPoints);
		// System.out.println("mariaPoints "+mariaPoints);

		if (andreaPoints > mariaPoints)
			return "Andrea";
		else if (andreaPoints < mariaPoints)
			return "Maria";
		else
			return "Tie";

	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter Inputs ");
		int n1, n2;

		n1 = scanner.nextInt();
		int a[] = new int[n1];
		for (int i = 0; i < n1; i++) {
			a[i] = scanner.nextInt();
		}

		n2 = scanner.nextInt();
		int b[] = new int[n2];
		for (int i = 0; i < n2; i++) {
			b[i] = scanner.nextInt();
		}

		String code = "";
		code = scanner.next();

		System.out.println(winner(a, b, code));

	}

}