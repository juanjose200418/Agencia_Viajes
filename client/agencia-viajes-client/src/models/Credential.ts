import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject("Credential")
export class Credential {

  @JsonProperty("email", String)
  email: string = "";

  @JsonProperty("password", String)
  password: string = "";
  
  constructor(email: string, password: string) {
    this.email = email;
    this.password = password;
  }
}