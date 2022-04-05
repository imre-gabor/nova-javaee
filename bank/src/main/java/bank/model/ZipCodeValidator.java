package bank.model;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ZipCodeValidator implements ConstraintValidator<ContainsZipCode, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null)
			return true;
		
		for(String part: value.split(" ")) {
			if(part.length() == 4) {
				try {
					Integer.parseInt(part);
					return true;
				}catch (Exception e) {
				}
			}
		}
		
		return false;
	}

}
