import { JsonObject, JsonProperty } from 'json2typescript';
import { Role } from './Role';

@JsonObject("User")
export class User {
  
  @JsonProperty("id", Number, true)
  id?: number = undefined;               
  
  @JsonProperty("firstname", String, true)
  firstname?: string = undefined;         

  @JsonProperty("lastname", String, true)
  lastname?: string = undefined;          
  
  @JsonProperty("email", String)
  email: string = "";                     

  @JsonProperty("pass", String, true)
  pass?: string = undefined;              

  @JsonProperty("phone", String)
  phone?: string = undefined;             

  @JsonProperty("address", String)
  address?: string = undefined;           

  @JsonProperty("role", String)
  role?: Role = undefined;                
  
  @JsonProperty("deleted", Boolean, true)
  deleted?: boolean = false;              
}
