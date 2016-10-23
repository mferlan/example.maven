package de.atron.test.sample.command;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import de.atron.test.sample.MockedInterface;

@Component
public class MockedServiceCommandProvider implements CommandProvider {

	private MockedInterface mockedService;

	@Reference(service = MockedInterface.class)
	public void setMockedService(MockedInterface mockedService) {
		this.mockedService = mockedService;
	}

	@Override
	public String getHelp() {
		StringBuilder builder = new StringBuilder();
		builder.append("Mocked Interface service commands");
		builder.append("\n");
		builder.append("divide - Enter two values, divident and divisor, and you will get the result of division");
		builder.append("\n");
		return builder.toString();
	}

	public Object _divide(CommandInterpreter intp) {
		String dividendStr = intp.nextArgument();
		if (dividendStr == null) {
			intp.println("Please enter two values, divident and divisor");
			return null;
		}

		String divisorStr = intp.nextArgument();
		if (divisorStr == null) {
			intp.println("Please enter two values, divident and divisor");
			return null;
		}

		double dividend = Double.parseDouble(dividendStr);
		double divisor = Double.parseDouble(divisorStr);
		double result = mockedService.divide(dividend, divisor);
		intp.println("Result of division is: " + result);
		return result;
	}

}
