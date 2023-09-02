<template>
  <div class="container">
    <van-nav-bar
      title="关于我们"
    />
    <div class="detail-wrap">
      <div class="item">
        这是一个正在崛起，但又处于挣扎中的企业……<br/>
        等待……
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "process",

  data() {
    return {
    };
  },

  created(){
  },

  methods: {

  }
}
</script>

<style lang="scss" scoped>
.container {
  padding: 20px;
  .detail-wrap {
    .item {
      color: #838485;
    }
  }
}
</style>
