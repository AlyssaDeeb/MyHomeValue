# MyHomeValue

My Home Value is separated into two components, the Java backend and React frontend. The Java portion is intended to be deployed on Apache Tomcat and the front end is powered by Node. 

The API keys, database information, and URLs have been removed from this code. In order to replicate this project, replace the following information:

Java: 
  - GreatSchools: APIKey
  - ZillowImages: APIKey
  - GoogleGeocode: APIkey
  - WalkScore: APIKey
  - DataProvider: baseURL
  - AuthGranter: URL, client_key, secret_key
  - DBConnection: user_name, password, port, host, dbName
  - AddressProvider: baseURL

React:
  - StreetView: GoogleMapsAPI
  - Places: client_id, client_secret, v
  - MapComps: key
  - Map: key
  - App: id
  - config: otherURL, baseURL, prodURL
  
To set up the database, use the file in [/MySQL](https://github.com/AlyssaDeeb/MyHomeValue/blob/master/MySQL/createTable.sql)

Code Contributors: Alyssa Deeb, Andrew Mehta, & Jimmy Sigala

Design Contributors: Adoriel Bethishou, Alyssa Deeb, Kim Lam, Andrew Mehta, Jimmy Sigala, Jean Truong
