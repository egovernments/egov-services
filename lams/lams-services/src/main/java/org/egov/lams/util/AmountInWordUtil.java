package org.egov.lams.util;

import java.text.DecimalFormat;
import java.text.FieldPosition;

import org.springframework.stereotype.Component;


public class AmountInWordUtil {
	
	private static final long ZEROS = 0;
	private static final long UNITS = 1;
	private static final long TENS = 10 * UNITS;
	private static final long HUNDREDS = 10 * TENS;
	private static final long THOUSANDS = 10 * HUNDREDS;
	private static final long TENTHOUSANDS = 10 * THOUSANDS;
	private static final long LAKHS = 10 * TENTHOUSANDS;
	private static final long TENLAKHS = 10 * LAKHS;
	private static final long CRORES = 10 * TENLAKHS;
	private static final long TENCRORES = 10 * CRORES;
	private static final long HUNDREDCRORES = 10 * TENCRORES;
	private static final long THOUSANDCRORES = 10 * HUNDREDCRORES;
	private static final long TENTHOUSANDCRORES = 10 * THOUSANDCRORES;

	private static final String[] CARDINAL = { "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen", "Twenty", "Thirty",
			"Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety", "Hundred" };

	/**
	 * Format decimal values.
	 * @param value the value
	 * @return the string
	 */
	public static String formatDecimal(final Double value) {
		final DecimalFormat formater = new DecimalFormat("###0.00");
		final FieldPosition _fldPos = new FieldPosition(0);
		final StringBuffer _adaptor = new StringBuffer();
		formater.format(value, _adaptor, _fldPos);
		return _adaptor.toString();
	}

	/**
	 * Amount in words.
	 * @param number the number
	 * @return the string
	 */
	public static String amountInWords(final Double number) {
		return AmountInWordUtil.convertToWord(formatDecimal(Double.valueOf(number)));
	}

	/**
	 * Translate the given currency number to word .
	 * @param number the number
	 * @return the string
	 */
	public static String convertToWord(String number) {

		String paise = "";

		if (number.contains(".")) {
			final String[] splitPaise = number.split("[.]");
			if (splitPaise.length == 2) {
				if (!splitPaise[1].equals("00")) {
					paise = "and " + paiseInWords(splitPaise[1]) + " " + "Paise Only";
				}
			}
			number = splitPaise[0];
		}

		final String returnValue = translateToWord(number);

		return (paise.isEmpty() ? ("Rupees " + returnValue + " Only ") : ("Rupees " + returnValue + " " + paise));

	}

	/**
	 * Translate the given number to word. Decimal places not allowed, for decimal use {@see NumberToWord#convertToWord(String)}
	 * @param String number
	 * @return String word
	 **/
	public static String translateToWord(String number) {
		long num = 0L;
		try {
			num = Long.parseLong(number);
		} catch (final NumberFormatException e) {
			/*LOGGER.error("Invalid Number, Please enter a valid Number.");
			throw new ApplicationRuntimeException("Exception occurred in convertToWord", e);*/
		}

		Long subNum = 0L;
		String returnValue = "";

		if (Long.parseLong(number) == ZEROS || number.length() > 12) {
			returnValue += getWord(Long.parseLong(number));
		}

		while (num > 0 && number.length() <= 12) {
			number = "" + num;
			final long place = getPlace(number);

			if (place == HUNDREDCRORES || place == THOUSANDCRORES || place == TENTHOUSANDCRORES) {
				subNum = Long.parseLong("" + number.charAt(0));
				returnValue += getWord(subNum);
				if (place == HUNDREDCRORES) {
					num -= subNum * HUNDREDCRORES;
					if (num == 0) {
						returnValue += " Hundred Crores ";
					} else {
						returnValue += " Hundred ";
					}
				} else if (place == THOUSANDCRORES) {
					num -= subNum * THOUSANDCRORES;
					if (num == 0) {
						returnValue += " Thousand Crores ";
					} else {
						returnValue += " Thousand  ";
					}
				} else {
					returnValue = "";
					subNum = Long.parseLong(number.charAt(0) + "" + number.charAt(1));
					num -= subNum * THOUSANDCRORES;
					if (subNum >= 21 && (subNum % 10) != 0 && num == 0) {
						returnValue += getWord(Long.parseLong(String.valueOf(number.charAt(0))) * 10) + " " + getWord(subNum % 10) + " Thousand Crores ";
					} else if (num == 0) {
						returnValue += getWord(Long.parseLong(String.valueOf(number.charAt(0))) * 10) + " " + " Thousand Crores ";
					} else {
						returnValue += getWord(Long.parseLong(String.valueOf(number.charAt(0))) * 10) + " " + getWord(subNum % 10) + " Thousand  ";
					}
				}

			} else if (place == TENS || place == TENTHOUSANDS || place == TENLAKHS || place == TENCRORES) {

				subNum = Long.parseLong(String.valueOf(number.charAt(0)) + String.valueOf(number.charAt(1)));

				if (subNum >= 21 && (subNum % 10) != 0) {
					returnValue += getWord(Long.parseLong(String.valueOf(number.charAt(0))) * 10) + " " + getWord(subNum % 10);
				} else {
					returnValue += getWord(subNum);
				}

				if (place == TENS) {
					num = 0;
				} else if (place == TENTHOUSANDS) {
					num -= subNum * THOUSANDS;
					returnValue += " Thousands ";
				} else if (place == TENLAKHS) {
					num -= subNum * LAKHS;
					returnValue += " Lakhs ";
				} else if (place == TENCRORES) {
					num -= subNum * CRORES;
					returnValue += " Crores ";
				}

			} else {
				subNum = Long.parseLong(String.valueOf(number.charAt(0)));
				returnValue += getWord(subNum);
				if (place == UNITS) {
					num = 0;
				} else if (place == HUNDREDS) {
					num -= subNum * HUNDREDS;
					returnValue += " Hundred ";
				} else if (place == THOUSANDS) {
					num -= subNum * THOUSANDS;
					returnValue += " Thousand ";
				} else if (place == LAKHS) {
					num -= subNum * LAKHS;
					returnValue += " Lakh ";
				} else if (place == CRORES) {
					num -= subNum * CRORES;
					returnValue += " Crore ";
				}
			}
		}
		return returnValue;
	}

	/**
	 * Normalize the unformatted number by removing comma and space.
	 * @param number the number
	 * @return the string
	 */
	private static String normalize(final String number) {
		final String cleanedNumber = number.replace(',', ' ').replaceAll(" ", "");
		if (number.length() > 1 && cleanedNumber.startsWith("0")) {
			return cleanedNumber.replaceFirst("0", "");
		}
		return cleanedNumber;
	}

	/**
	 * this method returns the place where the number exists. it does by checking number length. Gets the place.
	 * @param number the number
	 * @return the place
	 */
	private static long getPlace(final String number) {
		switch (number.length()) {

		case 1:
			return UNITS;
		case 2:
			return TENS;
		case 3:
			return HUNDREDS;
		case 4:
			return THOUSANDS;
		case 5:
			return TENTHOUSANDS;
		case 6:
			return LAKHS;
		case 7:
			return TENLAKHS;
		case 8:
			return CRORES;
		case 9:
			return TENCRORES;
		case 10:
			return HUNDREDCRORES;
		case 11:
			return THOUSANDCRORES;
		case 12:
			return TENTHOUSANDCRORES;
		}
		return 0;
	}

	/**
	 * Gets the word.
	 * @param number the number
	 * @return the word
	 */
	public static String getWord(final Long number) {
		final int value = number.intValue();
		switch (value) {
		case 30:
			return CARDINAL[21];
		case 40:
			return CARDINAL[22];
		case 50:
			return CARDINAL[23];
		case 60:
			return CARDINAL[24];
		case 70:
			return CARDINAL[25];
		case 80:
			return CARDINAL[26];
		case 90:
			return CARDINAL[27];
		case 100:
			return CARDINAL[28];
		default:
			if ((value < 21)) {
				return CARDINAL[value];
			} else {
				return "";
			}
		}

	}

	/**
	 * Paise in words.
	 * @param paise the paise
	 * @return the string
	 */
	private static String paiseInWords(final String paise) {

		Long subNum = 0L;
		String returnValue = "";

		if (paise.length() >= 2) {
			subNum = Long.parseLong(paise.charAt(0) + "" + paise.charAt(1));
		} else {
			subNum = Long.parseLong(paise.charAt(0) + "");
		}

		if (subNum >= 21 && (subNum % 10) != 0) {
			returnValue += getWord(Long.parseLong("" + paise.charAt(0)) * 10) + " " + getWord(subNum % 10);

		} else {
			returnValue += getWord(subNum);

		}

		return returnValue;
	}

}
