import { JsonObject, JsonProperty } from 'json2typescript';

@JsonObject("FLight")
export class FLight {
  
  @JsonProperty("id", Number, true)
  id?: number = undefined;
  
  @JsonProperty("flightNumber", String, true)
  flightNumber?: string | null;

  @JsonProperty("departure", String, true)
  departure?: string | null;
  
  @JsonProperty("arrival", String)
  arrival: string = "";

  @JsonProperty("duration", Number, true)
  duration?: number | null; 

  @JsonProperty("price", Number, true)  
  price?: number | null;

  @JsonProperty("airline", String, true)  
  airline?: string | null;
  
  @JsonProperty("deleted", Boolean, true)
  deleted?: boolean = false;
}
