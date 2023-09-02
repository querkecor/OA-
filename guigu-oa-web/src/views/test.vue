<template>
  <div>
    <div>账号切换</div>
    <button @click="wjl()" type="default" size="mini">王经理</button>
    <button @click="admin()" type="default" size="mini">admin</button>
    <button @click="zzjl()" type="default" size="mini">张总经理</button>
    <button @click="lisi()" type="default" size="mini">李四</button>
    <div>当前token：{{ token }}</div>
  </div>
</template>

<script>
export default {
  name: "Test",

  data() {
    return {
      token: ''
    };
  },

  created(){
    this.token = window.localStorage.getItem('token')
  },

  methods: {
    wjl() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLM0NTAwMzew1FEqLU4t8kwBikGYeYm5qUAtiSm5mXlKtQBsarYdQgAAAA.aAHrRo5zgSggM3gucGygUJ7KVUVppoQ1mDawht1GI2Qlo1Fw68W83v9mfea7XEf2hOf7pxcs0dS-GnfjV5cbGw'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    admin() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLM0NTAwMzew1FEqLU4t8kwBikGYeYm5qUAtiSm5mXlKtQBsarYdQgAAAA.aAHrRo5zgSggM3gucGygUJ7KVUVppoQ1mDawht1GI2Qlo1Fw68W83v9mfea7XEf2hOf7pxcs0dS-GnfjV5cbGw'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    zzjl() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLM0sTQxNDQy0lEqLU4t8kxRsjKHMPMSc1OBWqoyEvPSixPzlGoBPWTGJUUAAAA.yU6fltNUQuyvPdO8bmsYJJPuf4p52uyXI1PvLcfaLmzzMbzPvK0-Q50GXyWGmIdfdEog9F1BMGFNWSAQhqJpzw'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    lisi() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLM0sTQxNDIx01EqLU4t8kxRsjKBMPMSc1OBWnIyizOVagHVkEWnQQAAAA.f8EWkoWHfhR4FUpa1nAx7WX0xEMGF6AB4NG76DIlG5OGOh4ZZrHNEyBeZbxrF2PzW29_2nIL3UvpGAFbA9Ue7A'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    }
  }
}
</script>
