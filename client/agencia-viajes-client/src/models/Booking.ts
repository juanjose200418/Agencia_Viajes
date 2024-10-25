import { JsonObject, JsonProperty } from 'json2typescript';

@JsonObject("Booking")
export class Booking {
  
  @JsonProperty("id", Number, true)
  id?: number = undefined;

  @JsonProperty("userId", Number, true)
  userId?: number | null = null;

  @JsonProperty("destinationId", Number, true)
  destinationId?: number = undefined

  @JsonProperty("hotelId", Number, true)
  hotelId?: number | null = null;

  @JsonProperty("flightId", Number, true)
  flightId?: number | null = null;

  @JsonProperty("deleted", Boolean, true)
  deleted?: boolean = false;

  @JsonProperty("totalCost", Number, true)
  totalCost?: number | null = null;
}
