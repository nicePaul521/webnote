<template>
  <div>
    <mavon-editor ref="md" v-model="note.contentMd" @save="saveNote" @imgAdd="imgAdd"></mavon-editor>
  </div>
</template>

<script>
export default {
  name: "NoteEdit",
  data(){
    return{
      note:{
        id: '',
        contentMd: '',
        contentHtml: ''
      }
    }
  },
  mounted() {
    if(this.$route.query.noteId){
      this.loadNote(this.$route.query.noteId)
    }
  },
  methods:{
    loadNote(id){
      this.note.id = id
      var _this = this
      this.axios.get('/note/'+id.toString())
      .then(function (response){
        if(response.data.status === 200){
          _this.note = response.data.object
        }
      })
      .catch(function (error){
        console.log(error)
      })
    },
    saveNote(value,render){
      var _this = this
      var url = '/update/content/note/' + this.note.id
      this.note.contetMd = value
      this.note.contentHtml = render
      this.axios.post(url,this.note)
      .then(function (response){
        if(response.data.status === 200){
          _this.$message({
            type: 'success',
            message: '保存成功'
          })
        }
      })
    },
    imgAdd(pos,$file){
      var _this = this
      var formData = new FormData();
      formData.append('image',$file);
      this.axios({
        url: 'pic/',
        method: 'post',
        data: formData,
        headers: { 'Content-Type': 'multipart/form-data' },
        }).then((response) => {
          if(response.status === 200){
            let url = response.data.object;
            _this.$refs.md.$img2Url(pos,url)
          }
          console.log(url)
      })
    }
  }
}
</script>

<style scoped>

</style>
