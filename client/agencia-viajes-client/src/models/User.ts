import { JsonObject, JsonProperty } from 'json2typescript';
import { Role } from './Role';

@JsonObject("User")
export class User {
  
  @JsonProperty("id", Number, true)
  id?: number = undefined;
  
 
  @JsonProperty("firstname", String, true)
  firstname?: string | null;

  @JsonProperty("lastname", String, true)
  lastname?: string | null;
  
  @JsonProperty("email", String)
  email: string = "";

  @JsonProperty("pass", String, true)
  pass?: string | null; 

  @JsonProperty("phone", String)
  phone?: string; 

  @JsonProperty("address", String)
  address?: string; 

  @JsonProperty("role", String)
  role?: Role;
  
  @JsonProperty("deleted", Boolean, true)
  deleted?: boolean = false;
}
