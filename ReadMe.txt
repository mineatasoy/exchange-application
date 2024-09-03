
EXCHANGE APPLICATION

Please take into consideration:

This application works on the 8082 (configured at application.properties file)

For the currency information "https://fixer.io/" has been used with a fee free account and it does not support requests with BASE currency, so for the requests the default value returned for base is EUR, this affects the calculations, we can use 'EUR' as base for testing to be sure. Also, this fee free account has a limited request count monthly, if it exceeds it is going to fail. It is possible to change the access key for another account in the application.properties file.
 
For the conversion history API, there is an H2 database configured and a data.sql file added to resources directory. At every start up it fills the table (CONVERSION) with dummy data. 


This project contains 3 APIs according to the requirements

All APIs are available at SWAGGER (OopenApi) link below

http://localhost:8082/swagger-ui/index.html


EXCHANGE RATE API
 
GET

INPUT:
	URL: http://localhost:8082/api/exchange-rate?base=EUR&target=TRY

OUTPUT:
	{
		"currency": "TRY",
		"rate": 37.508952
	}

CONVERSION API

POST

	URL: http://localhost:8082/api/conversion
	RequestBody: {
	  "base": "EUR",
	  "target": "USD",
	  "amount": 10000
	}


Response:
	{
		"transactionId": "2024-09-03-73ad1eb4-6d7c-44aa",
		"date": "2024-09-03",
		"base": "EUR",
		"target": "USD",
		"amount": 10000.0,
		"rate": 1.105614,
		"conversionCalculated": 11056.140000000001
	}



CONVERSION HISTORY API

GET

INPUT

	URL: http://localhost:8082/api/conversion-history?date=2024-09-02&pageNumber=1

	TransactionDate: 2024-09-02

OUTPUT
	{
	  "content": [
		{
		  "transactionId": "2024-09-02-39a884ab-77d0-49d5",
		  "date": "2024-09-02",
		  "base": "USD",
		  "target": "TRY",
		  "amount": 10000,
		  "rate": 37.619298,
		  "conversionCalculated": 376192.98
		},
		{
		  "transactionId": "2024-09-02-32b7f1f9-3123-4489",
		  "date": "2024-09-02",
		  "base": "TRY",
		  "target": "USD",
		  "amount": 10000,
		  "rate": 1.107236,
		  "conversionCalculated": 11072.36
		},
		{
		  "transactionId": "2024-09-02-f2f54071-a917-419f",
		  "date": "2024-09-02",
		  "base": "TRY",
		  "target": "USD",
		  "amount": 10000,
		  "rate": 1.106623,
		  "conversionCalculated": 11066.23
		},
		{
		  "transactionId": "2024-09-02-8850ce3c-acc4-4254",
		  "date": "2024-09-02",
		  "base": "TRY",
		  "target": "CHF",
		  "amount": 10000,
		  "rate": 0.940357,
		  "conversionCalculated": 9403.57
		}
	  ],
	  "page": {
		"size": 10,
		"number": 1,
		"totalElements": 14,
		"totalPages": 2
	  }
	}


