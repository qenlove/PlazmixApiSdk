# PlazmixApiSdk
SDK for working with Plazmix API // api-docs.plazmix.net

This SDK aims to simplify API calls

###### Libraries used:
- **AsyncHttpClient** for Rest API communication
- **Google Gson** for response deserialization
- **Google Guava** for Preconditions checks
- **Lombok** (compilation only) for reducing mess

###### ToDo:
- [ ] Reduce useless instantiations
- [ ] Implement straight deserialisation of responses to POJOs
- [ ] Implement other API methods
- [ ] Make detailed documentation
- [ ] Introduce Enums for constant values left out
- [ ] Create ability for passing own HttpClient to all request executors

###### Examples:
OAuth URL generation and token request example:
https://github.com/qenlove/PlazmixApiSdk/blob/dev/example/src/main/java/me/qenlove/plazmix_sdk_example/OAuthExample.java

User methods call example:
https://github.com/qenlove/PlazmixApiSdk/blob/dev/example/src/main/java/me/qenlove/plazmix_sdk_example/UserExample.java
