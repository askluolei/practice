<template>
  <div class="docs-wrapper">
    <iframe :src="baseAPI + '/swagger-ui/index.html'" width="100%" height="900" seamless target="_top" title="Swagger UI" class="border-0"></iframe>
    <!-- <iframe src="/swagger-ui/index.html" width="100%" height="900" seamless target="_top" title="Swagger UI" class="border-0"></iframe> -->
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  export default {
    computed: {
      ...mapGetters([
        'baseAPI'
      ])
    }
  }
</script>

<style lang="scss">
  .docs-wrapper {
    margin: 10px 20px;
  }
</style>