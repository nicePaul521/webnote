<template>
  <div class="nav-bar">
    <el-menu
      :default-active="'/home'"
      class="el-menu-demo"
      mode="horizontal"
      router
      active-text-color="#25567A">
      <el-menu-item v-for="(item,i) in navList" :index="item.url" :key="i">
        {{ item.name }}
      </el-menu-item>
      <el-submenu index="2" style="float: right;">
        <template slot="title">{{userFlag.name}}</template>
        <el-menu-item v-for="(item,i) in userFlag.menuList" :index="item.url" :key="i">
          {{item.name}}
        </el-menu-item>
        <el-menu-item :style="{display:isLogin}" @click="logout">注销</el-menu-item>
      </el-submenu>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: "NavMenu",
  data() {
    return {
      isLogin: 'none',
      navList:[
        {name:'首页',url:'/home'},
        {name:'书架',url:'/bookshelf'}
      ],
      userFlag:{
        name: '',
        menuList: [
        ]
      }
    };
  },
  mounted() {
    if(window.localStorage.getItem("user")!=null){
      this.userFlag.name = JSON.parse(window.localStorage.getItem("user")).username
      this.userFlag.menuList=[
        {url: '/home',name: '用户中心'},
        {url: '/home',name: '笔记管理'}
      ]
      this.isLogin = 'inline-block'
    }
    else{
      this.userFlag.name = "未登陆",
      this.userFlag.menuList=[
        {url: '/register',name: '注册'},
        {url: '/login',name: '登陆'}
      ]
      this.isLogin = 'none'
    }
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
    logout(){
      var _this = this
      this.axios.get('/logout')
      .then(function (response){
        if(response.data.status === 200){
          _this.$store.commit('logout')
          _this.$router.replace('/login')
        }
      })
      .catch(function (error){
        console.log(error)
      })
    }
  }
}
</script>

<style scoped>
  .nav-bar{
    width: 100%;
    left: 0px;
    top: 0px;
  }
  .el-menu-item{
    font-weight: bolder;
  }
</style>
