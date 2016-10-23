#Registration of console command provider

Osgi Framework defines possibility to extend console commands by registering OSGI service that implements CommandProvider interface.

The interface contains only methods for the help. The console should use inspection to find the commands. All public commands, starting with a '_' and taking a CommandInterpreter as parameter will be found. E.g.

	 public Object _hello( CommandInterpreter intp ) {
	 	return "hello " + intp.nextArgument();
	 }
 
So we defined class in module *sample.service*

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
	
## Test command interpreter

Install sample project

	mvn -f sample/pom.xml clean install
Build equinox.application

	mvn -f equinox.application/pom.xml clean verify
Under equinox.application/target/products/.../ find eclipse.exe and execute it. Try console commands to analyze system.

Execute console command
	
	divide 15 4
	
