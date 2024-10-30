import { JsonObject, JsonProperty } from 'json2typescript';

@JsonObject("Hotel")
export class Hotel {

  @JsonProperty("id", Number, true)
  id?: number = undefined;

  @JsonProperty("name", String, true)
  name?: string = undefined;

  @JsonProperty("email", String, true)
  email?: string = undefined;

  @JsonProperty("address", String, true)
  address?: string = undefined;

  @JsonProperty("phone", String, true)
  phone?: string = undefined;

  @JsonProperty("location", String, true)
  location?: string = undefined;

  @JsonProperty("description", String, true)
  description?: string = undefined;

  @JsonProperty("cancellationPolicy", String, true)
  cancellationPolicy?: string = undefined;

  @JsonProperty("freeWifi", Boolean, true)
  freeWifi?: boolean = undefined;

  @JsonProperty("breakfastIncluded", Boolean, true)
  breakfastIncluded?: boolean = undefined;

  @JsonProperty("pool", Boolean, true)
  pool?: boolean = undefined;

  @JsonProperty("gym", Boolean, true)
  gym?: boolean = undefined;

  @JsonProperty("spa", Boolean, true)
  spa?: boolean = undefined;

  @JsonProperty("restaurant", Boolean, true)
  restaurant?: boolean = undefined;

  @JsonProperty("roomService", Boolean, true)
  roomService?: boolean = undefined;

  @JsonProperty("laundryService", Boolean, true)
  laundryService?: boolean = undefined;

  @JsonProperty("airportShuttle", Boolean, true)
  airportShuttle?: boolean = undefined;

  @JsonProperty("parking", Boolean, true)
  parking?: boolean = undefined;

  @JsonProperty("specialPackages", String, true)
  specialPackages?: string = undefined;

  @JsonProperty("recreationalActivities", String, true)
  recreationalActivities?: string = undefined;

  @JsonProperty("eventsConferences", String, true)
  eventsConferences?: string = undefined;

  @JsonProperty("guestReviews", String, true)
  guestReviews?: string = undefined;

  @JsonProperty("socialMedia", String, true)
  socialMedia?: string = undefined;

  @JsonProperty("languages", String, true)
  languages?: string = undefined;

  @JsonProperty("accessibility", String, true)
  accessibility?: string = undefined;

  @JsonProperty("deleted", Boolean, true)
  deleted?: boolean = false;

  @JsonProperty("destinationId", Number, true)
  destinationId?: number = undefined;
}
