<template>
  <div class="map">
    <v-container fluid class="header">
      <v-layout row wrap>
        <v-flex xs12>
          <h2>Select the Longitude and Latitude fields from your stream :</h2>
        </v-flex>
        <v-flex xs6>
          <v-select :items="columns" dense v-model="longitude" v-bind:label="longitudeTitle" autocomplete v-bind:disabled="isDisabled"></v-select>
        </v-flex>
        <v-flex xs6>
          <v-select :items="columns" dense v-model="latitude" v-bind:label="latitudeTitle" autocomplete v-bind:disabled="isDisabled"></v-select>
        </v-flex>
      </v-layout>
    </v-container>
    <div class="map-area" id="map-area">
    </div>
  </div>
</template>

<script>
import mapboxgl from 'mapbox-gl/dist/mapbox-gl'
import EventBus from '../event-bus'

export default {
  data() {
    return {
      zoom: 2,
      center:  [0, 0],
      latitude: null,
      longitude: null
    }
  },
  watch: {
    logs () {
      if (this.longitude && this.latitude) {
        this.map.getSource('logs').setData(this.data)
      }
    }
  },
  computed: {
    latitudeTitle() {
      return (this.$store.getters.columns.length > 0) ? 'Latitude' : 'Latitude (loading ...)'
    },
    longitudeTitle() {
      return (this.$store.getters.columns.length > 0) ? 'Longitude' : 'Longitude (loading ...)'
    },
    isDisabled() {
      return this.columns.length === 0
    },
    columns: function () {
      return this.$store.getters.columns
    },
    logs: function () {
      return this.$store.getters.logs
    },
    data: function () {
      if (this.longitude && this.latitude) {
        return {
          "type": "FeatureCollection",
          "features": this.logs.map(e => ({
            "type": "Feature",
            "properties": {},
            "geometry": {
              "type": "Point",
              "coordinates": [Number( e[this.longitude]), Number(e[this.latitude])]
            }
          }))
        }
      } else {
        return [];
      }
    }
  },
  created() {
    // Start stream
    EventBus.$emit('ws-action', 'on')
  },
  mounted() {
    mapboxgl.accessToken = this.$route.query['mapbox_token']
    this.map = new mapboxgl.Map({
      container: 'map-area',
      center: this.center,
      zoom: this.zoom,
      style: 'mapbox://styles/mapbox/dark-v9'
    });
    
    this.map.on('load', function () {
      this.map.addSource('logs', {
        type: 'geojson',
        data: null
      });
      this.map.addLayer({
          "id": "logs-heat",
          "type": "heatmap",
          "source": "logs",
          "maxzoom": 9,
          "paint": {
              // Increase the heatmap weight based on frequency and property magnitude
              "heatmap-weight": [
                  "interpolate",
                  ["linear"],
                  ["get", "mag"],
                  0, 0,
                  6, 1
              ],
              // Increase the heatmap color weight weight by zoom level
              // heatmap-intensity is a multiplier on top of heatmap-weight
              "heatmap-intensity": [
                  "interpolate",
                  ["linear"],
                  ["zoom"],
                  0, 1,
                  9, 3
              ],
              // Color ramp for heatmap.  Domain is 0 (low) to 1 (high).
              // Begin color ramp at 0-stop with a 0-transparancy color
              // to create a blur-like effect.
              "heatmap-color": [
                  "interpolate",
                  ["linear"],
                  ["heatmap-density"],
                  0, "rgba(33,102,172,0)",
                  0.2, "rgb(103,169,207)",
                  0.4, "rgb(209,229,240)",
                  0.6, "rgb(253,219,199)",
                  0.8, "rgb(239,138,98)",
                  1, "rgb(178,24,43)"
              ],
              // Adjust the heatmap radius by zoom level
              "heatmap-radius": [
                  "interpolate",
                  ["linear"],
                  ["zoom"],
                  0, 2,
                  9, 20
              ],
              // Transition from heatmap to circle layer by zoom level
              "heatmap-opacity": [
                  "interpolate",
                  ["linear"],
                  ["zoom"],
                  7, 1,
                  9, 0
              ],
          }
      }, 'waterway-label');

      this.map.addLayer({
          "id": "logs-point",
          "type": "circle",
          "source": "logs",
          "minzoom": 7,
          "paint": {
              // Size circle radius by earthquake magnitude and zoom level
              "circle-radius": [
                  "interpolate",
                  ["linear"],
                  ["zoom"],
                  7, [
                      "interpolate",
                      ["linear"],
                      ["get", "mag"],
                      1, 1,
                      6, 4
                  ],
                  16, [
                      "interpolate",
                      ["linear"],
                      ["get", "mag"],
                      1, 5,
                      6, 50
                  ]
              ],
              // Color circle by earthquake magnitude
              "circle-color": [
                  "interpolate",
                  ["linear"],
                  ["get", "mag"],
                  1, "rgba(33,102,172,0)",
                  2, "rgb(103,169,207)",
                  3, "rgb(209,229,240)",
                  4, "rgb(253,219,199)",
                  5, "rgb(239,138,98)",
                  6, "rgb(178,24,43)"
              ],
              "circle-stroke-color": "white",
              "circle-stroke-width": 1,
              // Transition from heatmap to circle layer by zoom level
              "circle-opacity": [
                  "interpolate",
                  ["linear"],
                  ["zoom"],
                  7, 0,
                  8, 1
              ]
          }
      }, 'waterway-label');
    
    }.bind(this));
  }
}
</script>

<style scoped>
.map {
  margin: -30px 50px 0 50px;
  background-color: #FFF;
  border: 1px solid rgba(0, 40, 100, 0.12);
  height: calc( 100% - 80px);
  border-radius: 2px;
  overflow: auto;
  display: flex;
  flex-flow: column;
  text-align: left;
}
.header {
  flex: 0 1 auto;
}
.header h2 {
  text-align: left;
  font-size : 1em;
}
.map-area {
  flex: 1;
  padding: 0;
}
.leaflet-pane {
  height: 300px;
}
.map-area div {
  position: absolute;
}
</style>
