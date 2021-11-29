<template>
  <div>

    <div style="position:absolute;float: left;top:20px;">
<!--      <P v-if="isShowUploadVideo" class="text">请保证视频格式是.mp4，且不超过20M</P>-->
      <div>
        <el-button  @click="add" icon="el-icon-plus" ></el-button>
      </div>
    </div>

    <div v-for="(item,i) in itemList" :key="i">
      <video :id="'myVideo'+item.videoId" class="video-js vjs-big-play-centered"></video>
      <div class="name"><el-tag type="info">{{item.videoName}}</el-tag></div>
      <div class="audio"><el-button type="primary" plain @click="push()">语音处理</el-button></div>
      <div class="edit"><el-button type="primary" icon="el-icon-edit" circle slot="reference" @click="modifyData(item)"></el-button></div>
      <div class="delete"><el-button type="danger" icon="el-icon-delete" circle slot="reference" @click="handleDelete(item.videoId)"></el-button></div>
    </div>



<!--        <template slot-scope="scope">-->
<!--          <video style="width: 30px; height: 30px" :src="scope.row.itemVideo" :preview-src-list="[scope.row.itemVideo]">-->
<!--            <div slot="error" class="image-slot">-->
<!--              <i class="el-icon-picture-outline"></i>-->
<!--            </div>-->
<!--          </video>-->
<!--        </template>-->

    <el-dialog :append-to-body="true" :visible.sync="dialogFormVisible"  title="新增教学视频">
      <el-form :model="form"  ref="form" @submit.native.prevent>

        <el-form-item label="视频名称" :label-width="formLabelWidth" prop="videoName">
          <el-input v-model="form.videoName" auto-complete="off" @keyup.enter.native="update"></el-input>
        </el-form-item>

        <el-form-item label="视频地址" :label-width="formLabelWidth" prop="videoUrl">
          <el-input v-model="form.videoUrl" autocomplete="off" placeholder="视频 URL"></el-input>
          <el-upload
            class="avatar-uploader el-upload--text"
            :action="uploadUrl"
            :show-file-list="false"
            accept=".mp4"
            :on-success="handleVideoSuccess"
            :before-upload="beforeUploadVideo"
            :on-progress="uploadVideoProcess"
          >
            <video
              v-if="showVideoPath != '' && !videoFlag"
              :src="showVideoPath"
              type="video/mp4"
              class="avatar video-avatar"
              controls="controls"
              preload="auto"
              width="200px"
              height="160px"
            >
              您的浏览器不支持视频播放
            </video>
            <!-- <i
              v-else-if="showVideoPath == '' && !videoFlag"
              class="el-icon-plus avatar-uploader-icon"
            ></i> -->
            <el-progress
              v-if="videoFlag == true"
              type="circle"
              :percentage="videoUploadPercent"
              style="margin-top:30px;"
            ></el-progress>
            <el-button
              type="primary"
              class="video-btn"
              slot="trigger"
              size="small"
              v-if="isShowUploadVideo"
            >
              选取文件<i class="el-icon-upload el-icon--right"></i
            ></el-button>
          </el-upload>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <!-- 设置触发更新的方法 -->
        <el-button type="primary"  @click="update('showVideoPath')" style="color: black;">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog  :append-to-body="true" :visible.sync="centerDialogVisible" title="修改视频信息">
      <el-form  :model="editForm"  ref="editForm" @submit.native.prevent>
        <el-form-item label="视频名称" prop="videoName">
          <el-input v-model="editForm.videoName"  @keyup.enter.native="submitEditRow()"></el-input>
        </el-form-item>
      </el-form>
      <div>
        <el-button @click="closeDialog()">取消</el-button>
        <el-button type="primary"  @click="submitEditRow()">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        itemList:[
          {
            videoId:1,
            videoName:"",
            videoUrl:"",
            videoSize:"",
            videoAddtime:"",
            videoStatus:"",
            videoLength:"",
            videoDuration:0,
            videoPoster:"",
            userId:1,
            videoType:""
          },
          // {
          //   videoId:1,
          //   videoName:"java教程",
          //   videoUrl:"http://localhost:8181/api/file/java教程.mp4",
          //   videoSize:"",
          //   videoAddtime:"2021-02-20 17:40:51",
          //   videoStatus:"init",
          //   videoLength:"0",
          //   videoDuration:0,
          //   videoPoster:"",
          //   userId:1,
          //   videoType:""
          // },
          // {
          //   videoId:2,
          //   videoName:"恭喜发财",
          //   videoUrl:"http://localhost:8181/api/file/恭喜发财.mp4",
          //   videoSize:"",
          //   videoAddtime:"2021-02-20 17:40:51",
          //   videoStatus:"init",
          //   videoLength:"0",
          //   videoDuration:0,
          //   videoPoster:"",
          //   userId:1,
          //   videoType:""
          // }
        ],
        formLabelWidth:"80px",
        dialogFormVisible: false,
        form: {
          videoUrl:'',
          videoName:'',
          userId:0,
        },
        centerDialogVisible:false,
        editForm:{
          videoId:0,
          videoName:"",
          // videoUrl:"",
          // videoSize:"",
          // videoAddtime:"",
          // videoStatus:"",
          // videoLength:"",
          // videoDuration:0,
          // videoPoster:"",
          // userId:1,
          // videoType:""
        },
        uploadUrl: "http://localhost:8181/upload", //你要上传视频到你后台的地址
        videoFlag: false, //是否显示进度条
        videoUploadPercent: "", //进度条的进度，
        isShowUploadVideo: true, //显示上传按钮
        showVideoPath: "" // 视频地址
        // showVideoPath: "https://aweme.snssdk.com/aweme/v1/playwm/?video_id=v0200fed0000bsgf4g46hqrc1h8adp50&ratio=720p&line=0" // 视频地址
      };
    },

    created () {
      const _this=this
      axios.get('http://localhost:8181/allVideo/'+this.$store.getters.getUserId).then(function (resp) {
        console.log(resp)
        _this.itemList=resp.data
        _this.$nextTick(() =>{
          _this.initVideo()
        })
      })
    },

    // mounted() {
    //   const _this=this
    //   axios.get('http://localhost:8181/allVideo/1').then(function (resp) {
    //     console.log(resp)
    //     _this.itemList=resp.data
    //     _this.$nextTick(() =>{
    //       _this.initVideo()
    //     })
    //   })
    // },
    // mounted () {
    //   this.$nextTick(() =>{
    //     this.initVideo()
    //   })
    // },

    beforeDestroy() {
      //控制 看你页面上显示的多少个  就循环多少次销毁
      this.itemList.map((item,i)=>{
        this.$video('myVideo'+`${item.videoId}`).dispose()
      })
    },
    methods: {
      initVideo() {
        //初始化视频方法 循环列表获取每个视频的id
        this.itemList.map((item,i)=>{
          let myPlayer = this.$video('myVideo'+item.videoId, {
            //确定播放器是否具有用户可以与之交互的控件。没有控件，启动视频播放的唯一方法是使用autoplay属性或通过Player API。
            controls: true,
            //自动播放属性,muted:静音播放
            //autoplay: "muted",
            //建议浏览器是否应在<video>加载元素后立即开始下载视频数据。
            preload: "auto",
            //设置视频播放器的显示宽度（以像素为单位）
            width: "200px",
            //设置视频播放器的显示高度（以像素为单位）
            height: "160px",
            //封面图
            sources:[
              {
              src:item.videoUrl,
            }
            ]
          });
        })
      },
      //上传前回调
      beforeUploadVideo(file) {
        const isLt40M = file.size / 1024 / 1024 < 40;
        if (["video/mp4"].indexOf(file.type) == -1) {
          //'video/ogg', 'video/flv', 'video/avi', 'video/wmv', 'video/rmvb'
          this.$message.error("请保证视频格式是.mp4哦!");
          return false;
        }
        if (!isLt40M) {
          this.$message.error("上传视频大小不能超过40MB哦!");
          return false;
        }
        this.isShowUploadVideo = false;
      },

      //进度条
      uploadVideoProcess(event, file, fileList) {
        this.videoFlag = true;
        this.videoUploadPercent = file.percentage.toFixed(0) * 1;
      },

      //上传成功回调
      handleVideoSuccess(res, file) {
        this.isShowUploadVideo = true;
        this.videoFlag = false;
        this.videoUploadPercent = 0;
        if (res.errorCode == "0") {
          this.showVideoPath = res.data;
          this.form.videoUrl=res.data;
        } else {
          this.$message.error("视频上传失败，请重新上传！");
        }
      },
      add() {
        this.form = {
          videoUrl:"",
          videoName:"",
          userId:this.$store.getters.getUserId
        };
        this.showVideoPath="";
        //   设置点击按钮之后进行显示对话框
        this.dialogFormVisible = true;
      },
      update() {
        axios.post('http://localhost:8181/insertVideo', this.form).then(function (resp) {
          console.log(resp);
          // this.itemList.push(this.form);
        })
        this.dialogFormVisible = false;
        location.reload()
      },
      cancel() {
        // 取消的时候直接设置对话框不可见即可
        this.dialogFormVisible = false;
      },
      handleDelete (videoId) {
        // 设置类似于console类型的功能
        this.$confirm("确定删除该视频?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            // 移除对应索引位置的数据，可以对row进行设置向后台请求删除数据
            axios.post('http://localhost:8181/deleteVideo/'+videoId).then(function (resp){
              console.log(resp);
            })
            this.$message({
              type: "success",
              message: "删除成功!"
            });
            location.reload()
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除"
            });
          });
      },
      modifyData(item) {
        this.centerDialogVisible = true
        this.editForm.videoId=item.videoId;
        this.editForm.videoName = item.videoName;
        // this.editForm.videoUrl=item.videoUrl;
        // this.editForm.videoSize = item.videoSize;
        // this.editForm.videoAddtime=item.videoAddtime;
        // this.editForm.videoStatus = item.videoStatus;
        // this.editForm.videoLength = item.videoLength;
        // this.editForm.videoDuration= item.videoDuration;
        // this.editForm.videoPoster = item.videoPoster;
        // this.editForm.userId = item.userId;
        // this.editForm.videoType = item.videoType;
      },
      submitEditRow() {
        axios.post('http://localhost:8181/updateVideo', this.editForm).then(function (resp) {
          console.log(resp);
        })
        this.centerDialogVisible = false;
        location.reload()
      },
      closeDialog(){
        this.centerDialogVisible=false
        console.log("editfrom",this.editForm)
      },
      push(){
        this.$router.push("/audioeditor")
      }
    }
  };
</script>

<style scoped>
  .video-js{
    margin: 20px 50px 100px 50px;
    float: left;
  }
  .edit{
    float: left;
    position: relative;
    top:200px;
    left:-140px;
  }
  .delete{
    float: left;
    position: relative;
    top:200px;
    left:-130px;
  }
  .name{
    float: left;
    width: 2px;
    position: relative;
    left:-175px;
    top:-20px;
  }
  .audio{
    float: left;
    width: 2px;
    position: relative;
    top:200px;
    left: -250px;
  }
</style>
