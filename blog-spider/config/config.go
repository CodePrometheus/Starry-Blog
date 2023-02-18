package config

import (
	"blog-spider/utils"
	"flag"
	"fmt"
	"github.com/fsnotify/fsnotify"
	"github.com/spf13/viper"
	"go.uber.org/zap/zapcore"
)

var Conf = new(config)

type config struct {
	System *System     `mapstructure:"system" json:"system"`
	Logs   *LogsConfig `mapstructure:"logs" json:"logs"`
	Mongo  *Mongo      `mapstructure:"mongo" json:"mongo"`
	Thrift *Thrift     `mapstructure:"thrift" json:"thrift"`
}

func InitConfig() {
	envList := []string{"dev", "prod"}
	env := flag.String("e", "dev", "input run env[dev|prod]:")
	flag.Parse()
	if f := utils.InSlice(envList, *env); false == f {
		panic(utils.StringToInterface("env input error"))
	}
	configName := "blog.conf.dev"
	switch *env {
	case "dev":
		configName = "blog.conf.dev"
	case "prod":
		configName = "blog.conf.prod"
	}
	viper.SetConfigName(configName)
	viper.SetConfigType("yml")
	viper.AddConfigPath("config")
	// 读取配置信息
	err := viper.ReadInConfig()
	if err != nil {
		panic(utils.StringToInterface(err.Error()))
	}

	// 热更新配置
	viper.WatchConfig()
	viper.OnConfigChange(func(e fsnotify.Event) {
		// 将读取的配置信息保存至全局变量Conf
		if err := viper.Unmarshal(Conf); err != nil {
			panic(fmt.Errorf("初始化配置文件失败:%s", err))
		}
	})

	if err != nil {
		panic(fmt.Errorf("读取配置文件失败:%s", err))
	}
	// 将读取的配置信息保存至全局变量Conf
	if err := viper.Unmarshal(Conf); err != nil {
		panic(fmt.Errorf("初始化配置文件失败:%s", err))
	}
}

type System struct {
	Environment string `mapstructure:"environment" json:"environment"`
	Port        string `mapstructure:"port" json:"port"`
}

type LogsConfig struct {
	Level      zapcore.Level `mapstructure:"level" json:"level"`
	Path       string        `mapstructure:"path" json:"path"`
	MaxSize    int           `mapstructure:"max-size" json:"maxSize"`
	MaxBackups int           `mapstructure:"max-backups" json:"maxBackups"`
	MaxAge     int           `mapstructure:"max-age" json:"maxAge"`
	Compress   bool          `mapstructure:"compress" json:"compress"`
}

type Mongo struct {
	Host     string `mapstructure:"host" json:"host"`
	Port     int    `mapstructure:"port" json:"port"`
	Username string `mapstructure:"username" json:"username"`
	Password string `mapstructure:"password" json:"password"`
}

type Thrift struct {
	Host string `mapstructure:"host" json:"host"`
	Port string `mapstructure:"port" json:"port"`
}
