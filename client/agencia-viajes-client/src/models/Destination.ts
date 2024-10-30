import { JsonObject, JsonProperty } from 'json2typescript';

@JsonObject("Destination")
export class Destination {
  
  @JsonProperty("id", Number, true)
  id?: number = undefined;                
  
  @JsonProperty("name", String, true)
  name?: string = undefined;              

  @JsonProperty("description", String, true)
  description?: string = undefined;       
  
  @JsonProperty("location", String)
  location: string = "";                  

  @JsonProperty("popularAttractions", String, true)
  popularAttractions?: string = undefined; 

  @JsonProperty("averageCost", Number, true)  
  averageCost?: number = undefined;       
  
  @JsonProperty("deleted", Boolean, true)
  deleted?: boolean = false;              
}
