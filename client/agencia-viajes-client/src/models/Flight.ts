import { JsonObject, JsonProperty } from 'json2typescript';

@JsonObject("Flight")
export class Flight {
  
  @JsonProperty("id", Number, true)         
  id?: number = undefined;                 
  
  @JsonProperty("flightNumber", String, true) 
  flightNumber?: string = undefined;        

  @JsonProperty("departure", String, true)  
  departure?: string = undefined;           
  
  @JsonProperty("arrival", String)          
  arrival: string = "";                     

  @JsonProperty("duration", Number, true)   
  duration?: number = undefined;            

  @JsonProperty("price", Number, true)      
  price?: number = undefined;               

  @JsonProperty("airline", String, true)    
  airline?: string = undefined;             
  
  @JsonProperty("deleted", Boolean, true)   
  deleted?: boolean = false;                
}
