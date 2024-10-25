import { JsonObject, JsonProperty } from 'json2typescript';

@JsonObject("Destination")
export class Destination {
  
  @JsonProperty("id", Number, true)
  id?: number = undefined;
  
  @JsonProperty("name", String, true)
  name?: string | null;

  @JsonProperty("description", String, true)
  description?: string | null;
  
  @JsonProperty("location", String)
  location: string = "";

  @JsonProperty("popularAttractions", String, true)
  popularAttractions?: string | null; 

  @JsonProperty("averageCost", Number, true)  
  averageCost?: number | null; 
  
  @JsonProperty("deleted", Boolean, true)
  deleted?: boolean = false;
}
