package bank.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CurrencyConverter implements AttributeConverter<Double, Double> {

	private static double rateOfStandardCurrency = 370.0;
	
	@Override
	public Double convertToDatabaseColumn(Double attribute) {
		return attribute/rateOfStandardCurrency;
	}

	@Override
	public Double convertToEntityAttribute(Double dbData) {
		return dbData * rateOfStandardCurrency;
	}

}
