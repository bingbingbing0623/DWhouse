<template>
  <div class="app-container">
    <!-- 查询条件居中显示 -->
    <el-row justify="center" style="padding-top: 5vh">
      <el-col :span="14">
        <el-form ref="form" :model="form" label-width="120px" class="query-form">
          <el-form-item label="电影名称">
            <el-autocomplete v-model="form.title" :fetch-suggestions="movieSearchSuggest" placeholder="请输入电影名"
                             style="width: 100%" clearable @select="handleSelect" size="small" />
          </el-form-item>
        </el-form>
        <!-- 查询按钮居中显示 -->
        <div style="text-align: center; margin-top: 20px">
          <el-button type="primary" @click="search(form)" size="small" plain>查询</el-button>
        </div>
      </el-col>
    </el-row>

    <!-- 查询结果和耗时对比并排显示 -->
    <el-row style="margin-top: 30px">
      <!-- 查询结果卡片 -->
      <el-col :span="12" style="padding-right: 10px">
        <el-card shadow="always" class="result-card">
          <h3 style="text-align: center; margin-bottom: 20px">查询结果</h3>
          <el-table :data="result" v-loading="isLoading" element-loading-text="正在为您查询..." stripe style="width: 100%" height="450">
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
        </el-card>
      </el-col>

      <!-- 耗时对比卡片 -->
      <el-col :span="12" style="padding-left: 10px">
        <el-card shadow="always" class="speed-card">
          <h3 style="text-align: center; margin-bottom: 20px">耗时对比</h3>
          <div id="speed" style="width: 100%; height: 400px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { title } from '@/settings';

export default {
  name: "movieSearch",
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
        title: "",
        columns: ["movies"], 
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
    neo4j_speed: {
      handler(newValue, oldValue) {
        this.neo4j_speed = newValue;
        this.echartsInit();
      },
    },
    mysql_speed: {
      handler(newValue) {
        this.mysql_speed = newValue;
        this.echartsInit();
      },
    },
    spark_speed: {
      handler(newValue) {
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

    movieSearchSuggest(queryString, cb) {
      this.$axios
          .get("/neo4j/actors/findMov", {
          })
          .then((res) => {
            var result = [];
            for (var i = 0; i < 100; i++) {
              if(res[i]!="#")
                  result.push({value: res[i]});
            }
            cb(result);
          })
          .catch((err) => {
            this.$message.error("当前网络异常，请稍后再试");
          });
    },

    search(form) {
      console.log(form);
      if (form.columns.length === 0) {
        this.$message.warning("请至少输入一个条件!");
      } else {
        this.isLoading = true;
        let data={
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
        };
        console.log("1111");
        console.log(data);
        this.$axios
          .get("/neo4j/actors/movie", {
            params:{
              title: form.title,
              page: 1,
              per_page: 10,
            }
          })
          .then((res) => {
            this.isLoading = false;
            console.log("这是结果", res);
            this.result = res.movieList;
            this.neo4j_speed = res.time;
          })
          .catch((err) => {
            this.$message.error("当前neo4j网络异常，请稍后再试");
          });

        this.$axios
            .post("/movie/detail", data)
            .then((res) => {
              //this.result = res.data;
              // if (this.columns["title"]) {
              //   this.result.forEach((movie) => {
              //     movie.movieTitle = movie.movieTitle.replace(/\"/g, "");
              //   });
              // }
              this.isLoading = false;
              this.mysql_speed = res.consuming_time;
            })
            .catch(() => {
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
              positive: form.positive / 100,
              page: 1,
              per_page: 10,
            })
            .then((res) => {
              this.result = res.data;
              // if (this.columns["title"]) {
              //   this.result.forEach((movie) => {
              //     movie.movieTitle = movie.movieTitle.replace(/\"/g, "");
              //   });
              // }
              this.isLoading = false;
              this.spark_speed = res.consuming_time;
            })
            .catch(() => {
              this.$message.error("当前hive网络异常，请稍后再试");
            });
       }
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
.app-container {
  padding: 20px;
}
</style>
