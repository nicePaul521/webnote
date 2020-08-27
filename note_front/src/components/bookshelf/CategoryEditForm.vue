<template>
  <div>
    <el-dialog title="编辑笔记类别" :visible.sync="dialogFormVisible">
      <el-form :model="form">
        <el-form-item label="笔记类别" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="onsubmit()">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "CategoryEditForm",
    data(){
      return{
        isCreate: false,
        dialogFormVisible: false,
        formLabelWidth: '120px',
        form: {
          id: '',
          name: ''
        }
      }
    },
    methods:{
      onsubmit(){
        var _this = this
        if(_this.isCreate){
          this.axios.post('category/add',_this.form)
          .then(function (response){
            if(response.data.status === 200){
              _this.dialogFormVisible = false
              _this.$message({
                type: 'success',
                message: '新增成功！'
              })
              _this.form = {
                id: '',
                name: ''
              }
              _this.$emit('update')
            }
          })
        }
        else{
          this.axios.post('category/update',_this.form)
            .then(function (response){
              if(response.data.status === 200){
                _this.dialogFormVisible = false
                _this.$message({
                  type: 'success',
                  message: '修改成功！'
                })
                _this.form = {
                  id: '',
                  name: ''
                }
                _this.$emit('update')
              }
            })
        }
      }
    }
  }
</script>

<style scoped>

</style>
