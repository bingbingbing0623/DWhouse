<template>
  <div class="app-container">
    <el-row justify="center" style="height: 50vh; padding-top: 5vh">
      <el-col :span="14">
        <el-form ref="form" :model="form" label-width="120px">
          <!-- 关系来源 -->
          <el-row gutter={20}>
            <el-col :span="12">
              <el-form-item label="关系来源">
                <el-select v-model="form.source" class="m-2" placeholder="Select" size="small" style="width: 75%" margin-right="15px">
                  <el-option label="演员" value="actor"></el-option>
                  <el-option label="导演" value="director"></el-option>
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="合作对象">
                <el-input v-model="form.target" class="m-2" size="small" style="width: 75%" :readonly="true" />
              </el-form-item>
            </el-col>
          </el-row>

          <!-- 查询按钮 -->
          <div style="text-align: center; margin-top: 10px">
            <el-button type="primary" @click="search(form)" size="small" style="margin-left: 3vh" plain>查询</el-button>
            <el-button type="primary" @click="searchTopRating(form)" size="small" style="margin-left: 3vh" plain>查询口碑最高</el-button>
          </div>

          <!-- 电影类型查询 -->
          <el-form-item label="电影类型" style="margin-top: 20px">
            <el-autocomplete v-model="form.genre" :fetch-suggestions="genreSearchSuggest" placeholder="请输入电影类型"
              style="width: 100%" clearable @select="handleSelect" size="small" />
          </el-form-item>

          <!-- 查询热门按钮 -->
          <div style="text-align: center; margin-top: 10px">
            <el-button type="primary" @click="searchPopular(form.genre)" size="small" plain>查询最受关注</el-button>
          </div>
        </el-form>
      </el-col>
    </el-row>

    <!-- 查询结果与耗时对比并排显示 -->
    <el-row justify="center" style="padding-top: 30px">
      <el-col :span="11">
        <el-card class="card-container" shadow="always">
          <h3>查询结果</h3>
          <el-table :data="result" v-loading="isLoading" element-loading-text="正在为您查询..." stripe style="width: 100%" height="400">
            <el-table-column prop="actorName1" label="演员1" width="100" />
            <el-table-column prop="actorName2" label="演员2/导演" width="100" />
            <el-table-column prop="collaborationCount" label="合作次数/总评论数" width="150" />
            <el-table-column prop="avgRating" label="平均给分" width="100" />
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="1">
        <el-divider direction="vertical" />
      </el-col>

      <el-col :span="11">
        <el-card class="card-container" shadow="always">
          <h3>耗时对比</h3>
          <div id="speed" style="width: 100%; height: 400px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
export default {
  name: "RelationSearch",
  data() {
    return {
      result: [
      {
        actorName1: '',  // 合作者1
        actorName2: '',  // 合作者2
        collaborationCount: 0  // 合作次数
      }
    ], 
      activeName: "search",
      isLoading: false,
      isTopRating: false,
      dialogVisible: false,
      result: [],
      mysql_speed: 0,
      spark_speed: 0,
      neo4j_speed: 0,
      form: {
        source: "actor",
        target: "演员",
        name: "",
        times: 5,
        columns:[""]
      },
      currentPage: 1,
      totalPage: 10,//改
    };
  },

  mounted() {
    this.echartsInit();
  },

  watch: {
    //监听速度变化，重新渲染页面
    mysql_speed: {
      handler(newValue, oldValue) {
        this.mysql_speed = newValue;
        this.echartsInit();
      },
    },
    spark_speed: {
      handler(newValue, oldValue) {
        this.spark_speed = newValue;
        this.echartsInit();
      },
    },
    neo4j_speed: {
      handler(newValue, oldValue) {
        this.neo4j_speed = newValue;
        this.echartsInit();
      },
    },
  },

  methods: {
    handleSelect(item) {
      console.log(item);
    },

    handleClick(tab, event) {
      console.log(tab, event);
    },

    search(form) {
      if (form.source == "director" && form.target == "director") {
        this.$message.warning("关系来源和合作对象不能同时为导演!");
      } else {
        this.isLoading = true;
        let queryData = {
          source: form.source,
          target: form.target,
          name: form.name,
          times: form.times,
        };
        console.log("queryData", queryData);
        //mysql关系查询
        this.$axios
            .post("/relation/detail", queryData)
            .then((res) => {
              console.log(res);
              console.log("mysql结果", this.result);
              this.mysql_speed = res.consuming_time;
              this.isLoading = false;
            })
            .catch((err) => {
              this.$message.error("当前mysql网络异常，请稍后再试");
            });
          this.$axios
          .post("/hive/movie/detail", {
              genre_name: form.genre,
              min_score: form.min_score,
              max_score: form.max_score,
              columns: [form.source],
              title: form.title,
              director: form.director,
              actor: form.actor,
              year: form.year,
              month: form.month,
              season: form.season,
              weekday: form.weekday,
              day: form.day,
              page: 1,
              per_page: 10,
            })
            .then((res) => {
              this.spark_speed = res.consuming_time;
            })
            .catch(() => {
              this.$message.error("当前spark网络异常，请稍后再试");
            });

        if (form.source == "actor") {
          // neo4j 关系查询
          this.$axios
            .get("/neo4j/actors/frequent-actors", {
              params: {
                page: 1,
                per_page: 5,
              }
            })
            .then((res) => {
              console.log("返回的数据:", res.collaborations);  // 打印返回的数据
              if (res && Array.isArray(res.collaborations)) {
                // 确保 collaborations 存在且是数组
                this.result = res.collaborations;  // 将 result 设置为 collaborations 数组
                this.neo4j_speed = res.time;
                this.isLoading = false;  // 停止加载
              } else {
                console.error("返回的数据不符合预期:", res);
                this.isLoading = false;  // 停止加载
              }
            })
            .catch((err) => {
              console.error("neo4j 查询失败:", err);
              this.$message.error("当前neo4j网络异常，请稍后再试");
              this.isLoading = false;
            });
        } else if (form.source == "director") {
          this.$axios
            .get("/neo4j/actors/frequent-directors", {
              params: {
                page: 1,
                per_page: 5,
              }
            })
            .then((res) => {
              console.log(res);
              this.result = res.collaborations.map(item => ({
                  actorName1: item.actorName,  // 将 actorName 重命名为 actorName1
                  collaborationCount: item.collaborationCount,
                  actorName2: item.directorName,
                }));
              this.neo4j_speed = res.time;
              this.isLoading=false;
            })
            .catch((err) => {
              console.error("neo4j 查询失败:", err);
              this.$message.error("当前neo4j网络异常，请稍后再试");
              this.isLoading = false;
            });
        }
      }
      this.isTopRating=false;
      
    },

    searchTopRating(form){
      if (form.source == "director" && form.target == "director") {
        this.$message.warning("关系来源和合作对象不能同时为导演!");
      } else {
        this.isLoading = true;

        let queryData = {
          source: form.source,
          target: form.target,
          name: form.name,
          times: form.times,
        };
        console.log("queryData", queryData);
        //mysql关系查询
        this.$axios
            .post("/relation/detail", queryData)
            .then((res) => {
              console.log(res);
              // this.result = res.data;
              console.log("mysql结果", this.result);
              this.mysql_speed = res.consuming_time;
              this.isLoading = false;
            })
            .catch((err) => {
              this.$message.error("当前mysql网络异常，请稍后再试");
            });

        if (form.source == "actor") {
          // neo4j 关系查询
          this.$axios
            .get("/neo4j/actors/toprating", {
              params: {
                page: 1,
                per_page: 5,
              }
            })
            .then((res) => {
              console.log("返回的数据:", res.actorCollaborationWithRating);  // 打印返回的数据
              if (res && Array.isArray(res.actorCollaborationWithRating)) {
                // 确保 collaborations 存在且是数组
                this.result = res.actorCollaborationWithRating ;  // 将 result 设置为 collaborations 数组
                this.neo4j_speed=res.time;
                this.isLoading = false;  // 停止加载
              } else {
                console.error("返回的数据不符合预期:", res);
                this.isLoading = false;  // 停止加载
              }
            })
            .catch((err) => {
              console.error("neo4j 查询失败:", err);
              this.$message.error("当前neo4j网络异常，请稍后再试");
              this.isLoading = false;
            });
        } else if (form.source == "director") {
          this.$message.warning("口碑查找不支持导演!");
        }
         this.$axios
            .get("/hive/relation/rating")
            .then((res) => {
              console.log(res);
              this.spark_speed = res.consuming_time;
            })
            .catch((err) => {
              this.$message.error("当前spark网络异常，请稍后再试");
            });
      }
      this.isTopRating=true;

    },


    searchPopular(genre) {
      this.isLoading = true;
      //mysql关系查询
      this.$axios
          .get("/relation/popular", {
            source: "actor",
            // target: form.target,
            // name: form.name,
            // times: form.times,
            params: {
              genre: genre,
              // page: 1,
              // per_page: 5,
            }
          })
          .then((res) => {
            console.log(res);
            console.log("mysql结果", this.result);
            this.mysql_speed = res.consuming_time;
            this.isLoading = false;
          })
          .catch((err) => {
            this.$message.error("当前mysql网络异常，请稍后再试");
          });



      //neo4j关系查询
      this.$axios
        .get("/neo4j/actors/most-attracted-group-2", {
          params: {
            genre: genre
          }
        })
        .then((res) => {
          console.log(res);
          this.result = res.doubleCollaboration.map(item => ({
                  actorName1: item.actorName1,  // 将 actorName 重命名为 actorName1
                  collaborationCount: item.avgReviews,
                  actorName2: item.actorName2,
                }));
          this.result=res.doubleCollaboration;
          this.neo4j_speed = res.time;
          this.isLoading=false;
        })
        .catch((err) => {
          this.$message.error("当前neo4j网络异常，请稍后再试");
        });

      //hive关系查询
      this.$axios
        .get("/hive/relation/popular", {
          params: {
            genre: genre
          }
        })
        .then((res) => {
          console.log(res);
          this.spark_speed = res.consuming_time;
        })
        .catch((err) => {
          this.$message.error("当前spark网络异常，请稍后再试");
        });
     },

    genreSearchSuggest(queryString, cb) {
      const fixedGenres = [
        "Comedy",
        "Action",
        "Special Interest",
        "Adventure",
        "Drama",
        "Romance",
        "Music Videos & Concerts",
        "Kids & Family",
        "Mystery & Thrillers",
        "Fantasy",
        "Anime & Manga",
        "Exercise & Fitness",
        "Documentary",
        "Action & Adventure"
      ];
      const result = fixedGenres.map((genre) => ({ value: genre }));

      // 调用回调函数返回结果
      cb(result);
    },

    

    echartsInit() {
      console.log("开始初始化");
      this.$echarts.init(document.getElementById("speed")).setOption({
        title: {
          text: "关系查询耗时对比(s)",
        },
        tooltip: {},
        xAxis: {
          data: ["mysql", "neo4j", "hive"],
        },
        yAxis: {},
        series: [
          {
            name: "查询耗时(s)",
            type: "bar",
            // data: [this.mysql_speed, this.neo4j_speed, this.spark_speed],
            data: [this.mysql_speed, this.neo4j_speed, this.spark_speed],
          },
        ],
      });
    },
  },
};
</script>

<style scoped>
.el-divider--vertical {
  height: 75vh;
}
</style>
