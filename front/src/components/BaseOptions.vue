<template>
  <div class="options">
    <label for="bootstrapServer">Bootstrap Servers (separate with comma) :</label>
    <input type="text" name="bootstrapServer" id="bootstrapServer" v-model="bootstrapServer">
    <label for="topic">Topic :</label>
    <input type="text" name="topic" id="topic" v-model="topic">
    <label for="data_type">Data type :</label>
    <select name="data_type" v-model="data_type">
      <option value="json">JSON</option>
      <option value="avro">Avro</option>
    </select>
    <label v-if="data_type === 'avro'" for="schema_registry_url">Schema registry url :</label>
    <input v-if="data_type === 'avro'" type="text" name="schema_registry_url" id="schema_registry_url" v-model="schema_registry_url">
  </div>
</template>

<script>
export default {
  name: "Options",
  props: {
    msg: String
  },
  data () {
    return {
      bootstrapServer: 'ten.s4m.priv',
      topic: 'services-lines',
      data_type: 'json',
      schema_registry_url: ''
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

label,
input,
select {
  display: block;
  width: calc(100% - 10px);
  padding: 5px;
  margin: 10px 0 ;
}
label {
  text-align: left;
}
</style>
