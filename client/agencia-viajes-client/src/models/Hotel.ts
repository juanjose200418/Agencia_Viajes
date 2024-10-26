import { JsonObject, JsonProperty } from 'json2typescript';

@JsonObject("Hotel")
export class Hotel {

  @JsonProperty("id", Number, true)
  id?: number = undefined;

  @JsonProperty("name", String, true)
  name?: string | null = null;

  @JsonProperty("email", String, true)
  email?: string | null = null;

  @JsonProperty("address", String, true)
  address?: string | null = null;

  @JsonProperty("phone", String, true)
  phone?: string | null = null;

  @JsonProperty("location", String, true)
  location?: string | null = null;

  @JsonProperty("description", String, true)
  description?: string | null = null;

  @JsonProperty("cancellationPolicy", String, true)
  cancellationPolicy?: string | null = null;

  @JsonProperty("freeWifi", Boolean, true)
  freeWifi?: boolean | null = null;

  @JsonProperty("breakfastIncluded", Boolean, true)
  breakfastIncluded?: boolean | null = null;

  @JsonProperty("pool", Boolean, true)
  pool?: boolean | null = null;

  @JsonProperty("gym", Boolean, true)
  gym?: boolean | null = null;

  @JsonProperty("spa", Boolean, true)
  spa?: boolean | null = null;

  @JsonProperty("restaurant", Boolean, true)
  restaurant?: boolean | null = null;

  @JsonProperty("roomService", Boolean, true)
  roomService?: boolean | null = null;

  @JsonProperty("laundryService", Boolean, true)
  laundryService?: boolean | null = null;

  @JsonProperty("airportShuttle", Boolean, true)
  airportShuttle?: boolean | null = null;

  @JsonProperty("parking", Boolean, true)
  parking?: boolean | null = null;

  @JsonProperty("specialPackages", String, true)
  specialPackages?: string | null = null;

  @JsonProperty("recreationalActivities", String, true)
  recreationalActivities?: string | null = null;

  @JsonProperty("eventsConferences", String, true)
  eventsConferences?: string | null = null;

  @JsonProperty("guestReviews", String, true)
  guestReviews?: string | null = null;

  @JsonProperty("socialMedia", String, true)
  socialMedia?: string | null = null;

  @JsonProperty("languages", String, true)
  languages?: string | null = null;

  @JsonProperty("accessibility", String, true)
  accessibility?: string | null = null;

  @JsonProperty("deleted", Boolean, true)
  deleted?: boolean = false;

  @JsonProperty("destinationId", Number, true)
  destinationId?: number | null = null;
}
