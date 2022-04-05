package bank.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

@Constraint(validatedBy = {} /*ZipCodeValidator.class*/)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Pattern(regexp = "(.+\\s)?\\d\\d\\d\\d(\\s.+)?", message = " nem tartalmaz irányítószámot")
public @interface ContainsZipCode {

	public String message() default "{bank.model.ContainsZipCode.message}";
	public Class<?>[] groups() default {};
	public Class<? extends Payload>[] payload() default { };
	
}
