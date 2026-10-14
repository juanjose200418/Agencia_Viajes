import { JsonObject, JsonProperty } from 'json2typescript';

@JsonObject("Destination")
export class Destination {
  
  @JsonProperty("id", Number, true)
  id?: number = undefined;                
  
  @JsonProperty("name", String, true)
  name?: string = undefined;              

  @JsonProperty("slug", String, true)
  slug?: string = undefined;

  @JsonProperty("country", String, true)
  country?: string = undefined;

  @JsonProperty("description", String, true)
  description?: string = undefined;       
  
  @JsonProperty("location", String)
  location: string = "";                  

  @JsonProperty("imageUrl", String, true)
  imageUrl?: string = undefined;

  @JsonProperty("pointsOfInterest", [String], true)
  pointsOfInterest?: string[] = undefined;

  @JsonProperty("averageCost", Number, true)  
  averageCost?: number = undefined;       
  
  @JsonProperty("deleted", Boolean, true)
  deleted?: boolean = false;              
}
