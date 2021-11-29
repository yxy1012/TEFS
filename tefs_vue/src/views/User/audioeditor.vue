<template>
  <div>
    <div v-if="active==1">
    <el-transfer class="transfer" v-model="value" :data="data" :titles="['未选取', '已选取']" ></el-transfer>
    </div>

    <div v-if="active==2">
<!--      <div style="display: inline-block;margin: 150px;">切分后的视频列表</div>-->
      <div v-for="(item,i) in cutList" :key="i">
        <div v-if="item.cutUrl!=='boundary'">
          <video :id="'cutVideo'+i" class="video-js vjs-big-play-centered"></video>
          <div class="extract"><el-button circle icon="el-icon-d-arrow-right" @click="extractText(i)"></el-button></div>
          <textarea ref="text" rows="7" cols="40" class="text" placeholder="Please input">{{paramsList[i]}}</textarea>
          <el-button class="record" icon="el-icon-microphone" circle @click="toRecord(i)"></el-button>
        </div>
        <div v-if="item.cutUrl==='boundary'"><el-tag type="info" class="cutname">{{videoNameList[num++]}}</el-tag></div>
      </div>
    </div>

    <div v-if="active==4">
      <Recorder :inputName="params" @child-event='parentEvent'></Recorder>
      <el-button round icon="el-icon-back" @click="backto2" class="back"></el-button>
    </div>

    <div v-if="active==3">
      <div class="success"><h3>合成成功！</h3></div>
      <div v-for="(item,i) in composedVideoList" :key="i">
        <div class="composedVideo">
          <video :id="'composedVideo'+i" class="video-js vjs-big-play-centered"></video>
        </div>
      </div>
    </div>

    <div class="steps">
    <el-steps :active="step" class="step">
      <el-step title="视频选取" icon="el-icon-document-add"></el-step>
      <el-step title="视频处理" icon="el-icon-scissors"></el-step>
      <el-step title="视频合成" icon="el-icon-files"></el-step>
    </el-steps>
    </div>
    <div class="button">
      <el-button style="margin-top: 12px;"  @click="next1" v-if="active===1">语音提取</el-button>
      <el-button style="margin-top: 12px;"  @click="next2" v-if="active===2">视频合成</el-button>
<!--      <el-button style="margin-top: 12px;"  @click="back"  v-if="active===3">上一步</el-button>-->
      <el-button style="margin-top: 12px;"  @click="finish"  v-if="active===3">完成</el-button>
      <el-button style="margin-top: 12px;"  @click="cancel" v-if="active!==3&&active!==4">取消</el-button>
    </div>
  </div>
</template>

<script>
  import Recorder from './recorder'
  export default {
    components:{Recorder},
    data() {
      return {
        data: [],
        value: [],
        active:1,
        step:1,
        num:0,
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
        ],
        selectedList:[],
        testList:[
          {
          videoId:0,
          videoName:"",
          videoUrl:"",
          videoSize:"",
          videoAddtime:"",
          videoStatus:"",
          videoLength:"",
          videoDuration:0,
          videoPoster:"",
          userId:0,
          videoType:""
          }
        ],
        cutList:[
          {
            cutUrl:"",
            cutParam:"",
          }
        ],
        paramsList:[],
        params:{
          cutUrl:"",
          index:0,
          text:"",
        },
        videoNameList:[],
        test:[
          {
            cutUrl:"",
            cutParam:"",
          }
        ],
        composedVideoList:[
          {
            videoId:0,
            videoName:"",
            videoUrl:"",
            videoSize:"",
            videoAddtime:"",
            videoStatus:"",
            videoLength:"",
            videoDuration:0,
            videoPoster:"",
            userId:0,
            videoType:""
        },
        ]
      };
    },
    created () {
      const _this=this
      axios.get('http://localhost:8181/allVideo/'+this.$store.getters.getUserId).then(function (resp) {
        console.log(resp)
        _this.itemList=resp.data
        _this.$nextTick(() =>{
          _this.itemList.map((item,i)=>{
            _this.data.push({
                  key: i,
                  label: item.videoName,
                });
          })
        })
      })
    },
    beforeDestroy () {
      this.cutList.map((item,i)=>{
        if(item.cutUrl!=="boundary") {
          this.$video('cutVideo' + i).dispose()
        }
      })
      this.composedVideoList.map((item,i)=>{
        this.$video('composedVideo'+i).dispose()
      })
      // this.cutList=[]
      // this.selectedList=[]
    },
    methods: {
      next1(){
        this.value.map((item,i)=>{
          this.selectedList.push(this.itemList[this.value[i]])
          this.videoNameList.push(this.itemList[this.value[i]].videoName)
        })
        const _this=this
        axios.post('http://localhost:8181/cutVideo',this.selectedList).then(function (resp) {
          _this.cutList=resp.data
          console.log(resp);
          _this.$nextTick(() =>{
            _this.initVideo()
          })
        })
        this.active=2
        this.step=2
      },
      next2(){
        this.$confirm('确定合成新视频?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {

          this.$message({
            type: 'success',
            message: '新视频合成成功!'
          });

          const _this = this;
          axios.post('http://localhost:8181/composeVideo',this.cutList).then(function(resp){
            console.log(resp);
            _this.composedVideoList=resp.data;
            _this.selectedList.map((item,i)=>{
              item.videoUrl=_this.composedVideoList[i].videoUrl;
            })
            console.log(1);
            console.log(_this.composedVideoList);
            _this.$nextTick(() =>{
              _this.initComposedVideo();
            })
          })

          this.cutList.map((item,i)=>{
            if(item.cutUrl!=="boundary") {
              this.$video('cutVideo' + i).dispose()
            }
          })
          this.videoNameList=[]
          this.num=0
          this.active=3
          this.step=3

        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消视频合成'
          });
        });
      },
      back(){
        this.value.map((item,i)=>{
          this.videoNameList.push(this.itemList[this.value[i]].videoName)
        })
        const _this=this
        axios.post('http://localhost:8181/cutVideo',this.selectedList).then(function (resp) {
          console.log(resp);
          _this.$nextTick(() =>{
            _this.initVideo()
          })
        })
        this.active=2
        this.step=2
        console.log(this.active)
      },
      toRecord(i){
        this.cutList.map((item,i)=>{
          if(item.cutUrl!=="boundary") {
            this.$video('cutVideo' + i).dispose()
          }
        })
        this.params.cutUrl=this.cutList[i].cutUrl
        this.params.index=i
        this.params.text=this.paramsList[i]
        this.videoNameList=[]
        this.num=0
        if(this.params!==null)
        {
          this.active=4
        }
      },
      backto2(){
        this.value.map((item,i)=>{
          this.videoNameList.push(this.itemList[this.value[i]].videoName)
        })
        const _this=this
        axios.post('http://localhost:8181/cutVideo',this.testList).then(function (resp) {
          console.log(resp);
          _this.$nextTick(() =>{
            _this.initVideo()
          })
        })
        this.active=2
      },
      finish(){
        console.log(2)
        console.log(this.selectedList)
        axios.post('http://localhost:8181/updateUrl',this.selectedList).then(function (resp) {
          console.log(resp);
        })
        this.composedVideoList.map((item,i)=>{
          this.$video('composedVideo'+i).dispose()
        })
        this.$router.push('/videobase')
      },
      cancel(){
        this.cutList.map((item,i)=>{
          if(item.cutUrl!=="boundary") {
            this.$video('cutVideo' + i).dispose()
          }
        })
        this.cutList=[]
        this.selectedList=[]
        this.videoNameList=[]
        this.paramsList=[]
        this.active=1;
        this.step=1;
        this.value=[];
        this.num=0;
      },
      initVideo() {
        //初始化视频方法 循环列表获取每个视频的id
        this.cutList.map((item,i)=>{
          this.paramsList.push({
            text:''
          })
          if(item.cutUrl!=="boundary") {
            let myPlayer = this.$video('cutVideo' + i, {
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
              sources: [
                {
                  src: item.cutUrl,
                }
              ]
            });
          }
        })
      },
      extractText(i){
        const _this=this
        axios.post('http://localhost:8181/extractText',this.cutList[i]).then(function (resp) {
          console.log(resp)
          _this.test=resp.data
          _this.paramsList[i].text=_this.test.cutParam
        })
      },
      parentEvent(data){
        this.cutList[data.index].cutUrl=data.cutUrl
      },
      initComposedVideo(){
        //初始化视频方法 循环列表获取每个视频的id
         this.composedVideoList.map((item,i)=>{
          let myPlayer = this.$video('composedVideo' + i, {
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
      }
    }
  }
</script>

<style scoped>
  .video-js{
    margin-left: 100px;
  }
  .transfer{
    text-align: left;
    position: relative;
    left:300px;
  }
  .step{
    text-align: left;
    /*position: absolute;*/
    /*top:580px;*/
    /*width: 1200px;*/
  }
  .steps{

  }
  .button{
    float: left;
    /*position: absolute;*/
    /*top:650px;*/
  }
  .extract{
    position: relative;
    top:-90px;
    left:-200px;
  }
  .text{
    z-index: 100;
    position: relative;
    top:-170px;
    left:30px;
    resize: none;
  }
  /*.params{*/
  /*  width: 100px;*/
  /*  height: 200px;*/
  /*}*/
  .record{
    position: relative;
    left: 250px;
    top:-220px;
  }
  .cutname{
    width: 1200px;
    position: relative;
    top:-20px;
  }
  .back{
    position: relative;
    top:-30px;
  }
  .composedVideo{
    position: relative;
    left:400px;
    padding-bottom: 50px;
  }
  .success{
    position: relative;
    top:-30px;
  }

</style>
