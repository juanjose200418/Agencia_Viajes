import { JsonObject, JsonProperty } from 'json2typescript';
import { Destination } from './Destination';

@JsonObject("RecommendationSearchResponse")
export class RecommendationSearchResponse {
  @JsonProperty("destinations", [Destination])
  destinations: Destination[] = [];

  @JsonProperty("explanation", String, true)
  explanation: string = '';
}