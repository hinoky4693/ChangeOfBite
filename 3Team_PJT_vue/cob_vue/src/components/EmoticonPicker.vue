<template>
  <div class="emoticon-picker" v-if="visible">
    <div class="emoticon" v-for="emo in emoticons" :key="emo" @click="select(emo)">
      <img :src="getEmoticonPath(emo)" :alt="emo" />
    </div>
  </div>
</template>

<script setup>
const emit = defineEmits(['select'])
const props = defineProps({ visible: Boolean })

const emoticons = ['heart', 'smile', 'cry', 'angry', 'thumbsup', 'thumbsdown', 'bear', 'laugh']

function select(name) {
  emit('select', name)
}
function getEmoticonPath(name) {
  return `/emoticons/${name}.png`
}
</script>

<style scoped>
.emoticon-picker {
  position: absolute;
  bottom: 60px;
  left: 10px;
  background: white;
  border: 1px solid #ccc;
  border-radius: 8px;
  display: flex;
  flex-wrap: wrap;
  padding: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.emoticon {
  width: 40px;
  height: 40px;
  margin: 4px;
  cursor: pointer;
}

.emoticon img {
  width: 100%;
  height: 100%;
}
</style>
