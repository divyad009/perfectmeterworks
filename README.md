# PerfectMeterWorks

Spring Boot website for Perfect Meter Works invoices.

## Run locally
1. Install JDK 17 and Maven.
2. Open a terminal in this project folder.
3. Run `mvn clean package`
4. Run `mvn spring-boot:run`
5. Open `http://localhost:8080`
6. Demo login: `admin` / `admin123`

## Important
- Change the demo login before deploying publicly. The credentials are currently configured in `SecurityConfig.java`.
- Generated PDFs are written to the `invoices` folder on the server and streamed to the browser for download.
- The current project has no database or online searchable invoice history.
- `invoice.html` is a clean replacement invoice layout using the new address. Your original logo/artwork was not included in the code you pasted, so add the approved logo asset and adjust the HTML if you need the exact previous template.
- This project uses Java 17. A hosted website lets the visitor use a browser, but the hosting provider runs the Java application; Windows 7 browser compatibility depends on the browser installed on that PC.
