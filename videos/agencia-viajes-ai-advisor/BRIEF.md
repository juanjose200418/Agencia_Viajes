# Agencia_Viajes AI Travel Advisor Promo

- Workflow: product-launch-video
- Format: landscape 1920x1080, 30 FPS
- Duration: exactly 10 seconds / 300 frames
- Language: Spanish
- Audio: original cinematic electronic score with restrained UI accents; no narration
- Product focus: the real intelligent travel search/advisor in Agencia_Viajes
- Main message: "Describo el viaje que quiero, la IA entiende la petición y recomienda destinos reales del catálogo."

## Verified Product Capability

Inspected implementation:

- Frontend route: `client/agencia-viajes-client/src/pages/search.vue`
- Frontend service: `client/agencia-viajes-client/src/services/RecommendationService.ts`
- Backend endpoint: `GET /recommendations/search?query=...`
- Backend service: `src/main/kotlin/com/example/service/RecommendIAService.kt`

The app supports a natural-language destination search. The backend asks OpenAI to choose up to 5 matching destinations from the real destination catalog and return a short Spanish explanation. If OpenAI is unavailable, it falls back to local matching over destination name, country, location, points of interest, and description.

The promo must not imply itinerary generation, booking/payment handling, or hotel reservation processing. It should show destination recommendations and the user moving from the AI advisor to destination/hotel exploration.

## Story

0-1.5s: Cinematic travel opening with "Tu próximo viaje empieza con una idea."

1.5-5.5s: The real AI search interface receives "Quiero una escapada de playa para este verano", submits it and shows the real loading/skeleton pattern.

5.5-8.3s: The same interface reveals the AI explanation and real `DestinationCard` visual pattern for Cancún, Bali and Phuket.

8.3-10s: Premium brand hold with "Agencia Viajes", "Descubre. Decide. Viaja." and "Impulsado por IA".

## Accuracy Constraints

- Keep the existing Vuetify dark theme, primary app bar, page copy, outlined field, search button, tonal AI card and destination card structure.
- Do not imply itinerary generation, booking, payment handling or autonomous reservation.
- Do not expose implementation details, endpoint paths, code, terminal UI or technology names.
- Do not invent prices or unsupported destination metadata.
