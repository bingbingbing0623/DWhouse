<template>
  <div class="app-container">
    <!-- 查询条件部分居中显示 -->
    <el-row justify="center" style="padding-top: 5vh">
      <el-col :span="20">
        <el-form ref="form" :model="form" label-width="200px" class="query-form">
          <el-form-item label="上映时间(按年份查询)">
            <el-row gutter={20}>
              <!-- 年份输入 -->
              <el-col :span="8">
                <el-input-number v-model="form.year" size="mini" :max="2022" :min="1930" controls-position="right"
                  style="width: 100%" placeholder="选择年份" />
              </el-col>
            </el-row>
          </el-form-item>
          <div style="text-align: center; margin-top: 20px">
          <el-button type="primary" @click="searchYear(form)" size="small" plain>查询</el-button>
        </div>
          
        </el-form>

        <!--按月份选择-->
        <el-form ref="form2" :model="form2" label-width="200px" class="queryMonth-form">
          <el-form-item label="上映时间(按月份查询)">
            <el-row gutter={20}>
              <el-col :span="8">
                <el-input-number v-model="form2.year" size="mini" :max="2022" :min="1930" controls-position="right"
                  style="width: 100%" placeholder="选择年份" />
              </el-col>
              <el-form-item label="月份选择：">
              <el-col :span="8">
                <el-input-number v-model="form2.month" size="mini" :max="12" :min="1" controls-position="right"
                  style="width: 100%" placeholder="选择月份" />
              </el-col>
            </el-form-item>
            </el-row>
          </el-form-item>
          <div style="text-align: center; margin-top: 20px">
          <el-button type="primary" @click="searchMonth(form2)" size="small" plain>查询</el-button>
        </div>
        </el-form>
          <!--按季度份选择-->
        <el-form ref="form3" :model="form3" label-width="200px" class="querySeason-form">
          <el-form-item label="上映时间(按季度查询)">
            <el-row gutter={20}>
              <el-col :span="8">
                <el-input-number v-model="form3.year" size="mini" :max="2022" :min="1930" controls-position="right"
                  style="width: 100%" placeholder="选择年份" />
              </el-col>
              <el-form-item label="季度选择：">
              <el-col :span="8">
                <el-input-number v-model="form3.season" size="mini" :max="4" :min="1" controls-position="right"
                  style="width: 100%" placeholder="选择季度" />
              </el-col>
            </el-form-item>
            </el-row>
          </el-form-item>
          <div style="text-align: center; margin-top: 20px">
          <el-button type="primary" @click="searchSeason(form3)" size="small" plain>查询</el-button>
        </div>
        </el-form>

        <el-form ref="form4" :model="form4" label-width="200px" class="queryWeek-form">
          <el-form-item label="上映时间(按周查询)">
            <el-row gutter={20}>
              <el-col :span="8">
                <el-input-number v-model="form4.year" size="mini" :max="2022" :min="1930" controls-position="right"
                  style="width: 100%" placeholder="选择年份" />
              </el-col>
              <el-form-item label="周几选择：">
              <el-col :span="8">
                <el-input-number v-model="form4.weekday" size="mini" :max="7" :min="1" controls-position="right"
                  style="width: 100%" placeholder="选择周几" />
              </el-col>
            </el-form-item>
            </el-row>
          </el-form-item>
          <div style="text-align: center; margin-top: 20px">
          <el-button type="primary" @click="searchWeekday(form4)" size="small" plain>查询</el-button>
        </div>
        </el-form>
      </el-col>
    </el-row>

    <!-- 查询结果和耗时对比并排显示 -->
    <el-row style="margin-top: 20px">
      <el-col :span="12">
        <div>
          <h3>查询结果</h3>
          <el-table :data="result" v-loading="isLoading" element-loading-text="正在为您查询..." stripe style="width: 100%"
            height="450">
            <!-- 电影名称 -->
            <el-table-column prop="movieTitle" label="电影名称" width="150" />
                
                <!-- 电影编号 -->
                <el-table-column prop="movieId" label="编号" width="130" />
                
                <!-- 评分 -->
                <el-table-column prop="movieRating" label="评分" width="100" />
                
                <!-- 评论数量 -->
                <el-table-column prop="movieReviewNum" label="评论数量" width="120" />
                
                <!-- 类型 -->
                <el-table-column prop="movieGenre" label="类型" width="200" />
                
                <!-- 发布日期 -->
                <el-table-column prop="movieDate" label="发布日期" width="120" />
                
                <!-- 语言 -->
                <el-table-column prop="movieLanguage" label="语言" width="120" />
                
                <!-- 适合年龄 -->
                <el-table-column prop="movieAge" label="适合年龄" width="120" />
                
                <!-- 时长 -->
                <el-table-column prop="movieRunTime" label="时长" width="100" />
                
                <!-- 媒体格式 -->
                <el-table-column prop="movieMediaFormat" label="媒体格式" width="140" />
                
                <!-- 电影公司 -->
                <el-table-column prop="movieStudio" label="电影公司" width="160" />
                
                <!-- 版本 -->
                <el-table-column prop="movieEdition" label="版本" width="120" />
          </el-table>
        </div>
      </el-col>

      <el-col :span="12">
        <div>
          <h3>耗时对比</h3>
          <div id="speed" style="width: 100%; height: 400px"></div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>




<script>
export default {
  name: "timeSearch",

  data() {
    return {
      activeName: "search_res",
      isLoading: false,
      totalPage: 10,
      currentPage: 1,
      neo4j_speed:0,
      mysql_speed: 0,
      spark_speed: 0,
      form: {
      year: null,
      columns:["date"]
      },
      form2: {
        year: null,
        month: null,
        columns:["date"]
      },
      form3: {
        year: null,
        season: null,
        columns:["date"]
      },
      form4: {
        year: null,
        weekday: null,
        columns:["date"]
      },
      columns: {
        movieId: true,
        movieScore: true,
        genre: true,
        reviewnum: true,
        movieTitle: true,
        movieDate: true,
        movieLanguage: true,
        movieAge: true,
        movieRunTime: true,
        movieMediaFormat: true,
        movieStudio: true,
        movieEdition: true,
      },
      result: [],
      test: "",
    };
  },

  mounted() {
    this.echartsInit();
  },

  watch: {
    //监听速度变化，重新渲染页面
    neo4j_speed: {
      handler(newValue, oldValue) {
        this.neo4j_speed = newValue;
        this.echartsInit();
      },
    },
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
  },

  methods: {
    handleSelect(item) {
      console.log(item);
    },

    handleClick(tab, event) {
      console.log(tab, event);
    },

    searchYear(form) {
        this.isLoading = true;
        console.log(form.year);
        //调用neo4j查询
        this.$axios
          .get("/neo4j/actors/year", {
            params:
            {year: form.year,
            page: 1,
            per_page: 10,}
          })
          .then((res) => {
            console.log("这是neo4j的结果", res);
            this.result=res.movieList;
            this.neo4j_speed = res.time;
            this.isLoading=false;
          })
          .catch((err) => {
            this.$message.error("当前neo4j网络异常，请稍后再试");
          });
        let questData = {
          genre_name: form.genre,
          min_score: form.min_score,
          max_score: form.max_score,
          columns: form.columns,
          title: form.title,
          director: form.director,
          actor: form.actor,
          year: form.year,
          month: form.month,
          season: form.season,
          weekday: form.weekday,
          day: form.day,
          positive: form.positive / 100,
        };
        console.log(questData);
        this.$axios
          .post("/movie/detail", questData)
          .then((res) => {
            console.log("这是mysql的结果", res);
            //this.result = res.data;
            this.mysql_speed = res.consuming_time;
            this.isLoading = false;
          }).catch(() => {
          this.$message.error("当前mysql网络异常，请稍后再试");
        });
        this.$axios
            .post("/hive/movie/detail", {
              genre_name: form.genre,
              min_score: form.min_score,
              max_score: form.max_score,
              columns: form.columns,
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
    },
    searchMonth(form2) {
        this.isLoading = true;
        console.log(form2.year);
        // 创建一个映射，数字对应月份的英文缩写
        const monthMap = {
          1: 'Jan', 2: 'Feb', 3: 'Mar', 4: 'Apr', 5: 'May', 6: 'Jun',
          7: 'Jul', 8: 'Aug', 9: 'Sep', 10: 'Oct', 11: 'Nov', 12: 'Dec'
        };
              // 检查用户输入的月份并转换为英文缩写
        const monthAbbreviation = monthMap[form2.month] || '';

        console.log(form2.year, monthAbbreviation); // 输出调试信息
              //调用neo4j查询
        this.$axios
          .get("/neo4j/actors/month", {
            params:
            {year: form2.year,
            month:monthAbbreviation,
            page: 1,
            per_page: 10,}
          })
          .then((res) => {
            console.log("这是neo4j的结果", res);
            this.result=res.movieList;
            this.neo4j_speed = res.time;
            this.isLoading=false;
          })
          .catch((err) => {
            this.$message.error("当前neo4j网络异常，请稍后再试");
          });
        let questData = {
          genre_name: form2.genre,
          min_score: form2.min_score,
          max_score: form2.max_score,
          columns: form2.columns,
          title: form2.title,
          director: form2.director,
          actor: form2.actor,
          year: form2.year,
          month: form2.month,
          season: form2.season,
          weekday: form2.weekday,
          day: form2.day,
          positive: form2.positive / 100,
        };
        console.log(questData);
        this.$axios
            .post("/movie/detail", questData)
            .then((res) => {
              console.log("这是mysql的结果", res);
              //this.result = res.data;
              this.mysql_speed = res.consuming_time;
              this.isLoading = false;
            }).catch(() => {
          this.$message.error("当前mysql网络异常，请稍后再试");
        });
        this.$axios
            .post("/hive/movie/detail", {
              genre_name: form2.genre,
              min_score: form2.min_score,
              max_score: form2.max_score,
              columns: form2.columns,
              title: form2.title,
              director: form2.director,
              actor: form2.actor,
              year: form2.year,
              month: form2.month,
              season: form2.season,
              weekday: form2.weekday,
              day: form2.day,
              page: 1,
              per_page: 10,
            })
            .then((res) => {
              this.spark_speed = res.consuming_time;
            })
            .catch(() => {
              this.$message.error("当前spark网络异常，请稍后再试");
            });
    },
    searchSeason(form3) {
      this.isLoading = true;

      // 映射季度到月份
      const seasonMap = {
        1: ['Jan', 'Feb', 'Mar'],
        2: ['Apr', 'May', 'Jun'],
        3: ['Jul', 'Aug', 'Sep'],
        4: ['Oct', 'Nov', 'Dec']
      };


      // 获取对应季度的月份
      const months = seasonMap[form3.season] || [];

      console.log(form3.year, form3.season, months); // 输出调试信息
       // 将月份数组转换为逗号分隔的字符串
      const seasonParam = months.join(',');

      // 调用neo4j查询
      this.$axios
        .get("/neo4j/actors/season", {
          params: {
            year: form3.year,
            season: seasonParam,  // 传递季度对应的月份数组
            page: 1,
            per_page: 10
          }
        })
        .then((res) => {
          console.log("这是neo4j的结果", res);
          this.result = res.movieList;
          this.neo4j_speed = res.time;
          this.isLoading = false;
        })
        .catch((err) => {
          this.$message.error("当前neo4j网络异常，请稍后再试");
        });


      let questData = {
        genre_name: form3.genre,
        min_score: form3.min_score,
        max_score: form3.max_score,
        columns: form3.columns,
        title: form3.title,
        director: form3.director,
        actor: form3.actor,
        year: form3.year,
        month: form3.month,
        season: form3.season,
        weekday: form3.weekday,
        day: form3.day,
        positive: form3.positive / 100,
      };
      console.log(questData);
      this.$axios
          .post("/movie/detail", questData)
          .then((res) => {
            console.log("这是mysql的结果", res);
            //this.result = res.data;
            this.mysql_speed = res.consuming_time;
            this.isLoading = false;
          }).catch(() => {
            this.$message.error("当前mysql网络异常，请稍后再试");
          });
          this.$axios
            .post("/hive/movie/detail", {
              genre_name: form3.genre,
              min_score: form3.min_score,
              max_score: form3.max_score,
              columns: form3.columns,
              title: form3.title,
              director: form3.director,
              actor: form3.actor,
              year: form3.year,
              month: form3.month,
              season: form3.season,
              weekday: form3.weekday,
              day: form3.day,
              page: 1,
              per_page: 10,
            })
            .then((res) => {
              this.spark_speed = res.consuming_time;
            })
            .catch(() => {
              this.$message.error("当前spark网络异常，请稍后再试");
            });

    },
    searchWeekday(form4) {
        this.isLoading = true;
        console.log(form4.year);
        this.$axios
          .get("/neo4j/actors/weekday", {
            params:
            {year: form4.year,
              weekday:form4.weekday,
            page: 1,
            per_page: 10,}
          })
          .then((res) => {
            console.log("这是neo4j的结果", res);
            this.result=res.movieList;
            this.neo4j_speed = res.time;
            this.isLoading=false;
          })
          .catch((err) => {
            this.$message.error("当前neo4j网络异常，请稍后再试");
          });
          console.log("NEO4Jover")
        let questData = {
          genre_name: form4.genre,
          min_score: form4.min_score,
          max_score: form4.max_score,
          columns: form4.columns,
          title: form4.title,
          director: form4.director,
          actor: form4.actor,
          year: form4.year,
          month: form4.month,
          season: form4.season,
          weekday: form4.weekday,
          day: form4.day,
          positive: form4.positive / 100,
        };
        console.log(questData);
        this.$axios
            .post("/movie/detail", questData)
            .then((res) => {
              console.log("这是mysql的结果", res);
              //this.result = res.data;
              this.mysql_speed = res.consuming_time;
              // this.isLoading = false;
            }).catch(() => {
          this.$message.error("当前mysql网络异常，请稍后再试");
        });
        this.$axios
            .post("/hive/movie/detail", {
              genre_name: form4.genre,
              min_score: form4.min_score,
              max_score: form4.max_score,
              columns: form4.columns,
              title: form4.title,
              director: form4.director,
              actor: form4.actor,
              year: form4.year,
              month: form4.month,
              season: form4.season,
              weekday: form4.weekday,
              day: form4.day,
              page: 1,
              per_page: 10,
            })
            .then((res) => {
              this.spark_speed = res.consuming_time;
            })
            .catch(() => {
              this.$message.error("当前spark网络异常，请稍后再试");
            });
    },

    search(form) {
      console.log(form);
      if (form.columns.length == 0) {
        this.$message.warning("请至少输入一个条件!");
      } else {
        this.isLoading = true;

        //判断年份是否为空
        if (form.year == 1930) {
          console.log("year为空");
          form.year = null;
          console.log(form.year);
        } else {
          console.log(form.year);
        }

        //判断月份或季度
        if (form.month_season == "") {
          console.log("月份季度为空");
          form.month = null;
          form.season = null;
        } else if (form.month_season == "month") {
          console.log("选择月份");
          form.season = null;
        } else if ((form.month_season = "season")) {
          console.log("选择季度");
          form.month = null;
        }

        //判断天数或星期几
        if (form.day_weekday == "") {
          console.log("天数为空");
          form.day = null;
          form.weekday = null;
        } else if (form.day_weekday == "day") {
          console.log("选择天数");
          form.weekday = null;
        } else if ((form.day_weekday = "weekday")) {
          console.log("选择周几");
          form.day = null;
        }

        for (var i in this.columns) {
          //清空原有条件
          this.columns[i] = false;
        }

        console.log("这是原始条件", form.columns);
        for (var i = 0; i < form.columns.length; i++) {
          this.columns[form.columns[i]] = true;
          console.log(
            "这是条件",
            form.columns[i],
            this.columns[form.columns[i]]
          );
        }

        //调用mysql查询总数
        this.$axios
          .post("/movie/count", {
            genre_name: form.genre,
            min_score: form.min_score,
            max_score: form.max_score,
            title: form.title,
            director: form.director,
            actor: form.actor,
            year: form.year,
            month: form.month,
            season: form.season,
            weekday: form.weekday,
            day: form.day,
            positive: form.positive / 100,
            page: 1,
            per_page: 10,
          })
          .then((res) => {
            console.log("pages:", res.pages);
            this.totalPage = res.pages;
          });

        //调用mysql查询
        this.$axios
          .post("/movie/detail", {
            genre_name: form.genre,
            min_score: form.min_score,
            max_score: form.max_score,
            columns: form.columns,
            title: form.title,
            director: form.director,
            actor: form.actor,
            year: form.year,
            month: form.month,
            season: form.season,
            weekday: form.weekday,
            day: form.day,
            positive: form.positive / 100,
            page: 1,
            per_page: 10,
          })
          .then((res) => {
            console.log("这是mysql的结果", res);
            console.log("data", res.data);
            this.isLoading = false;
            this.result = res.data;
            console.log("test", this.result);
            if (this.columns["title"] == true) {
              for (var i = 0; i < this.result.length; i++) {
                console.log("标题", this.result[i].movieTitle);
                this.result[i].movieTitle = this.result[i].movieTitle.replace(
                  /\"/g,
                  ""
                );
                console.log("标题后", this.result[i].movieTitle);
              }
            }
            this.isLoading = false;
            this.mysql_speed = res.consuming_time;
            console.log("mysql速度", this.mysql_speed);
          })
          .catch((err) => {
            this.$message.error("当前mysql网络异常，请稍后再试");
          });

        //调用hive查询
        this.$axios
          .post("/hive/movie/detail", {
            genre_name: form.genre,
            min_score: form.min_score,
            max_score: form.max_score,
            columns: form.columns,
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
            console.log("这是spark的结果", res);
            this.spark_speed = res.consuming_time;
          })
          .catch((err) => {
            this.$message.error("当前spark网络异常，请稍后再试");
          });
      }
    },

    getNewPage(form) {
      //分页获取数据
      console.log("切换页面");
      console.log("当前页数", this.currentPage);
      this.isLoading = true;
        // this.$axios
        //   .post("/movie/detail", {
        //     genre_name: form.genre,
        //     min_score: form.min_score,
        //     max_score: form.max_score,
        //     columns: form.columns,
        //     title: form.title,
        //     director: form.director,
        //     actor: form.actor,
        //     year: form.year,
        //     month: form.month,
        //     season: form.season,
        //     weekday: form.weekday,
        //     day: form.day,
        //     positive: form.positive / 100,
        //     page: this.currentPage,
        //     per_page: 10,
        //   })
        //   .then((res) => {
        //     console.log("这是mysql的结果", res);
        //     console.log("data", res.data);
        //     this.result = res.data;
        //     console.log(this.result);
        //     this.isLoading = false;
        //     this.mysql_speed = res.consuming_time;
        //   })
        //   .catch((err) => {
        //     this.$message.error("当前mysql网络异常，请稍后再试");
        //   });

        //   //调用hive查询
        // this.$axios
        //   .post("/hive/movie/detail", {
        //     genre_name: form.genre,
        //     min_score: form.min_score,
        //     max_score: form.max_score,
        //     columns: form.columns,
        //     title: form.title,
        //     director: form.director,
        //     actor: form.actor,
        //     year: form.year,
        //     month: form.month,
        //     season: form.season,
        //     weekday: form.weekday,
        //     day: form.day,
        //     page: this.currentPage,
        //     per_page: 10,
        //   })
        //   .then((res) => {
        //     console.log("这是spark的结果", res);
        //     this.spark_speed = res.consuming_time;
        //   })
        //   .catch((err) => {
        //     this.$message.error("当前spark网络异常，请稍后再试");
        //   });
    },

    echartsInit() {
      //使用时只需要把setOption里的对象换成echarts中的options或者自己的参数即可
      console.log("开始初始化");
      this.$echarts.init(document.getElementById("speed")).setOption({
        title: {
          text: "组合查询耗时对比(s)",
        },
        tooltip: {},
        xAxis: {
          data: ["mysql", "hive","neo4j"],
        },
        yAxis: {},
        series: [
          {
            name: "查询耗时(s)",
            type: "bar",
            data: [this.mysql_speed, this.spark_speed,this.neo4j_speed],
          },
        ],
      });
    },
  },
};
</script>

<style scoped>
.el-divider--vertical {
  height: 100vh;
}
</style>
