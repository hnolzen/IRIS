# IRIS Simulation Model
# R-Script for printing tick abundance

library(dplyr)
library(ggplot2)
library(stats)

# Select simulation options
smoothing <- FALSE
year <- 2018

# Set main directory of IRIS
iris_main_directory <- "C:/Users/nolzen/IdeaProjects/IRIS/"

# Set model output directory
output_directory <- paste0(iris_main_directory, "output")

# Set model plot directory
plot_directory <- paste0(iris_main_directory, "plots/timeseries")

# Get latest modified directory
directories <- file.info(list.dirs(output_directory, recursive = FALSE))
latest_directory_path <- rownames(directories)[which.max(directories$mtime)]

# Read output data
df <- read.table(file = paste0(latest_directory_path, "/iris_abundance_", year, ".csv") , sep = ",", header = T)

# Aggregate output data for each life cycle stage
sum_questing_larvae <- aggregate(df$questing_larvae, by = list(tick = df$tick), FUN = sum)
colnames(sum_questing_larvae) <- c("tick", "larvae")

sum_questing_nymphs <- aggregate(df$questing_nymphs, by = list(tick = df$tick), FUN = sum)
colnames(sum_questing_nymphs) <- c("tick", "nymphs")

sum_questing_adults <- aggregate(df$questing_adults, by = list(tick = df$tick), FUN = sum)
colnames(sum_questing_adults) <- c("tick", "adults")

sum_inactive_larvae <- aggregate(df$inactive_larvae, by = list(tick = df$tick), FUN = sum)
colnames(sum_inactive_larvae) <- c("tick", "inactive_larvae")

sum_inactive_nymphs <- aggregate(df$inactive_nymphs, by = list(tick = df$tick), FUN = sum)
colnames(sum_inactive_nymphs) <- c("tick", "inactive_nymphs")

sum_inactive_adults <- aggregate(df$inactive_adults, by = list(tick = df$tick), FUN = sum)
colnames(sum_inactive_adults) <- c("tick", "inactive_adults")

sum_fed_larvae <- aggregate(df$fed_larvae, by = list(tick = df$tick), FUN = sum)
colnames(sum_fed_larvae) <- c("tick", "fed_larvae")

sum_fed_nymphs <- aggregate(df$fed_nymphs, by = list(tick = df$tick), FUN = sum)
colnames(sum_fed_nymphs) <- c("tick", "fed_nymphs")

sum_fed_adults <- aggregate(df$fed_adults, by = list(tick = df$tick), FUN = sum)
colnames(sum_fed_adults) <- c("tick", "fed_adults")

sum_late_fed_larvae <- aggregate(df$late_fed_larvae, by = list(tick = df$tick), FUN = sum)
colnames(sum_late_fed_larvae) <- c("tick", "late_fed_larvae")

sum_late_fed_nymphs <- aggregate(df$late_fed_nymphs, by = list(tick = df$tick), FUN = sum)
colnames(sum_late_fed_nymphs) <- c("tick", "late_fed_nymphs")

abundance <- cbind(sum_questing_larvae, sum_questing_nymphs$nymphs, sum_questing_adults$adults,
                   sum_inactive_larvae$inactive_larvae, sum_inactive_nymphs$inactive_nymphs, sum_inactive_adults$inactive_adults,
                   sum_fed_larvae$fed_larvae, sum_fed_nymphs$fed_nymphs, sum_fed_adults$fed_adults,
                   sum_late_fed_larvae$late_fed_larvae, sum_late_fed_nymphs$late_fed_nymphs,
                   sum_inactive_larvae$inactive_larvae + sum_fed_larvae$fed_larvae + sum_late_fed_larvae$late_fed_larvae,
                   sum_inactive_nymphs$inactive_nymphs + sum_fed_nymphs$fed_nymphs + sum_late_fed_nymphs$late_fed_nymphs,
                   sum_inactive_adults$inactive_adults + sum_fed_adults$fed_adults)

colnames(abundance) <- c("tick",
                         "questing_larvae", "questing_nymphs", "questing_adults",
                         "inactive_larvae", "inactive_nymphs", "inactive_adults",
                         "fed_larvae", "fed_nymphs", "fed_adults",
                         "late_fed_larvae", "late_fed_nymphs",
                         "not_questing_larvae", "not_questing_nymphs", "not_questing_adults")

# Calculate moving average
if (smoothing) {
  k <- 10
  abundance$nymphs <- stats::filter(as.ts(abundance$nymphs), rep(1/k, k), sides = 2, circular = TRUE)
}

# Create plot of questing nymphs
abundance_plot_questing_nymphs <- ggplot(data = abundance) +
  geom_vline(xintercept = 31,  linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 59,  linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 90,  linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 120, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 151, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 181, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 212, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 243, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 273, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 304, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 334, linetype = "solid", size = 0.1, colour = "lightgray") +
  
  geom_line(size = 1, colour = "#4292c6", linetype = "solid", aes(x = tick, y = questing_nymphs)) +
  #geom_area(alpha = 1, fill = "#4292c6") +
  
  ggtitle("Simulated seasonal dynamics and maximum \n number of active nymphs in 2018 near Regensburg") +
  labs(x = "Time (days)",
       y = "Number of questing nymphs",
       color = "Stage",
       linetype = "") +
  theme_classic() +
  scale_y_continuous(breaks = seq(0, 10000, by = 1000), limits = c(0, 10500), expand = c(0, 0)) +
  scale_x_continuous(breaks = c(15, 31, 46, 59, 75, 90, 105, 120, 135, 151, 166, 181, 196, 212, 227, 243, 258, 273, 288, 304, 319, 334, 349, 365),
                     labels = c("Jan", "", "Feb", "", "Mar", "", "Apr", "", "May", "", "Jun", "", "Jul", "", "Aug", "", 
                                "Sep", "", "Oct", "", "Nov", "", "Dec", ""),
                     expand = c(0, 0), 
                     limits = c(0, 365)) + 
  theme(plot.title = element_text(colour = "black", size = 22, face = "bold", hjust = 0.5),
        plot.margin = unit(c(0.25, 0.75, 0.25, 0.25), "cm"),
        axis.text.x = element_text(colour = "black", size = 18, hjust = 0.5),
        axis.text.y = element_text(colour = "black", size = 14),
        axis.title.x = element_text(colour = "black", size = 24, face = "bold", margin = margin(t = 12, r = 0, b = 0, l = 0)),
        axis.title.y = element_text(colour = "black", size = 24, face = "bold", margin = margin(t = 0, r = 12, b = 0, l = 0)),
        axis.line = element_line(size = 0.5),
        axis.ticks = element_line(colour = "black"),
        panel.grid.major.y = element_line(colour = "#e4e4e4", size = 0.1),
        panel.border = element_rect(fill = NA, size = 0.5)) 

ggsave(filename = "abundance_questing_nymphs.pdf", device = "pdf", path = plot_directory,
       width = 35, height = 15, units = "cm")
abundance_plot_questing_nymphs
dev.off()

# Create plot of questing ticks (all life cycle stages)
abundance_plot_questing_ticks <- ggplot(data = abundance) +
  geom_vline(xintercept = 31,  linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 59,  linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 90,  linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 120, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 151, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 181, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 212, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 243, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 273, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 304, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 334, linetype = "solid", size = 0.1, colour = "lightgray") +
  
  geom_line(size = 1, colour = "#4292c6", linetype = "solid", aes(x = tick, y = questing_nymphs)) +
  geom_line(size = 1, colour = "#6baed6", linetype = "dotted", aes(x = tick, y = questing_larvae)) +
  geom_line(size = 1, colour = "#08519c", linetype = "dashed", aes(x = tick, y = questing_adults)) +
  
  ggtitle("Questing ticks") +
  labs(x = "Time (days)",
       y = "Number of questing ticks",
       color = "Stage",
       linetype = "") +
  theme_classic() +
  scale_y_continuous(breaks = seq(0, 10000, by = 1000), limits = c(0, 10500), expand = c(0, 0)) +
  scale_x_continuous(breaks = c(15, 31, 46, 59, 75, 90, 105, 120, 135, 151, 166, 181, 196, 212, 227, 243, 258, 273, 288, 304, 319, 334, 349, 365),
                     labels = c("Jan", "", "Feb", "", "Mar", "", "Apr", "", "May", "", "Jun", "", "Jul", "", "Aug", "", 
                                "Sep", "", "Oct", "", "Nov", "", "Dec", ""),
                     expand = c(0, 0), 
                     limits = c(0, 365)) + 
  theme(plot.title = element_text(colour = "black", size = 22, face = "bold", hjust = 0.5),
        plot.margin = unit(c(0.25, 0.75, 0.25, 0.25), "cm"),
        axis.text.x = element_text(colour = "black", size = 18, hjust = 0.5),
        axis.text.y = element_text(colour = "black", size = 14),
        axis.title.x = element_text(colour = "black", size = 24, face = "bold", margin = margin(t = 12, r = 0, b = 0, l = 0)),
        axis.title.y = element_text(colour = "black", size = 24, face = "bold", margin = margin(t = 0, r = 12, b = 0, l = 0)),
        axis.line = element_line(size = 0.5),
        axis.ticks = element_line(colour = "black"),
        panel.grid.major.y = element_line(colour = "#e4e4e4", size = 0.1),
        panel.border = element_rect(fill = NA, size = 0.5)) 

ggsave(filename = "abundance_questing_ticks.pdf", device = "pdf", path = plot_directory,
       width = 35, height = 15, units = "cm")
abundance_plot_questing_ticks
dev.off()

# Create plot of inactive ticks (all life cycle stages)
abundance_plot_inactive <- ggplot(data = abundance) +
  geom_vline(xintercept = 31,  linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 59,  linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 90,  linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 120, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 151, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 181, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 212, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 243, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 273, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 304, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 334, linetype = "solid", size = 0.1, colour = "lightgray") +
  
  geom_line(size = 1, colour = "#fecc5c", linetype = "dotted", aes(x = tick, y = inactive_larvae)) +
  geom_line(size = 1, colour = "#fd8d3c", linetype = "solid", aes(x = tick, y = inactive_nymphs)) +
  geom_line(size = 1, colour = "#f03b20", linetype = "dashed", aes(x = tick, y = inactive_adults)) +

  ggtitle("Inactive ticks") +
  labs(x = "Time (days)",
       y = "Number of inactive ticks",
       color = "",
       linetype = "Life Cyle Stage") +
  theme_classic() +
  scale_y_continuous(breaks = seq(0, 25000, by = 5000), limits = c(0, 25000), expand = c(0, 0)) +
  scale_x_continuous(breaks = c(15, 31, 46, 59, 75, 90, 105, 120, 135, 151, 166, 181, 196, 212, 227, 243, 258, 273, 288, 304, 319, 334, 349, 365),
                     labels = c("Jan", "", "Feb", "", "Mar", "", "Apr", "", "May", "", "Jun", "", "Jul", "", "Aug", "", 
                                "Sep", "", "Oct", "", "Nov", "", "Dec", ""),
                     expand = c(0, 0), 
                     limits = c(0, 365)) + 
  theme(plot.title = element_text(colour = "black", size = 22, face = "bold", hjust = 0.5),
        plot.margin = unit(c(0.25, 0.75, 0.25, 0.25), "cm"),
        axis.text.x = element_text(colour = "black", size = 18, hjust = 0.5),
        axis.text.y = element_text(colour = "black", size = 14),
        axis.title.x = element_text(colour = "black", size = 24, face = "bold", margin = margin(t = 12, r = 0, b = 0, l = 0)),
        axis.title.y = element_text(colour = "black", size = 24, face = "bold", margin = margin(t = 0, r = 12, b = 0, l = 0)),
        axis.line = element_line(size = 0.5),
        axis.ticks = element_line(colour = "black"),
        panel.grid.major.y = element_line(colour = "#e4e4e4", size = 0.1),
        panel.border = element_rect(fill = NA, size = 0.5)) 

ggsave(filename = "abundance_inactive_ticks.pdf", device = "pdf", path = plot_directory,
       width = 35, height = 15, units = "cm")
abundance_plot_inactive
dev.off()

# Create plot of fed ticks (all life cycle stages)
abundance_plot_fed <- ggplot(data = abundance) +
  geom_vline(xintercept = 31,  linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 59,  linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 90,  linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 120, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 151, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 181, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 212, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 243, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 273, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 304, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 334, linetype = "solid", size = 0.1, colour = "lightgray") +
  
  geom_line(size = 1, colour = "#bae4b3", linetype = "dotted", aes(x = tick, y = fed_larvae)) +
  geom_line(size = 1, colour = "#74c476", linetype = "solid", aes(x = tick, y = fed_nymphs)) +
  geom_line(size = 1, colour = "#31a354", linetype = "dashed", aes(x = tick, y = fed_adults)) +
  geom_line(size = 1, colour = "#9e9ac8", linetype = "dotted", aes(x = tick, y = late_fed_larvae)) +
  geom_line(size = 1, colour = "#756bb1", linetype = "solid", aes(x = tick, y = late_fed_nymphs)) +
  
  ggtitle("Fed ticks") +
  labs(x = "Time (days)",
       y = "Number of fed ticks",
       color = "Stage",
       linetype = "") +
  theme_classic() +
  scale_y_continuous(breaks = seq(0, 15000, by = 1000), limits = c(0, 15000), expand = c(0, 0)) +
  scale_x_continuous(breaks = c(15, 31, 46, 59, 75, 90, 105, 120, 135, 151, 166, 181, 196, 212, 227, 243, 258, 273, 288, 304, 319, 334, 349, 365),
                     labels = c("Jan", "", "Feb", "", "Mar", "", "Apr", "", "May", "", "Jun", "", "Jul", "", "Aug", "", 
                                "Sep", "", "Oct", "", "Nov", "", "Dec", ""),
                     expand = c(0, 0), 
                     limits = c(0, 365)) + 
  theme(plot.title = element_text(colour = "black", size = 22, face = "bold", hjust = 0.5),
        plot.margin = unit(c(0.25, 0.75, 0.25, 0.25), "cm"),
        axis.text.x = element_text(colour = "black", size = 18, hjust = 0.5),
        axis.text.y = element_text(colour = "black", size = 14),
        axis.title.x = element_text(colour = "black", size = 24, face = "bold", margin = margin(t = 12, r = 0, b = 0, l = 0)),
        axis.title.y = element_text(colour = "black", size = 24, face = "bold", margin = margin(t = 0, r = 12, b = 0, l = 0)),
        axis.line = element_line(size = 0.5),
        axis.ticks = element_line(colour = "black"),
        panel.grid.major.y = element_line(colour = "#e4e4e4", size = 0.1),
        panel.border = element_rect(fill = NA, size = 0.5)) 

ggsave(filename = "abundance_fed_ticks.pdf", device = "pdf", path = plot_directory,
       width = 35, height = 15, units = "cm")
abundance_plot_fed
dev.off()

# Create plot of late fed ticks (larvae and nymphs)
abundance_plot_late_fed <- ggplot(data = abundance) +
  geom_vline(xintercept = 31,  linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 59,  linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 90,  linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 120, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 151, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 181, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 212, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 243, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 273, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 304, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 334, linetype = "solid", size = 0.1, colour = "lightgray") +
  
  geom_line(size = 1, colour = "#9e9ac8", linetype = "dotted", aes(x = tick, y = late_fed_larvae)) +
  geom_line(size = 1, colour = "#756bb1", linetype = "solid", aes(x = tick, y = late_fed_nymphs)) +
  
  ggtitle("Late fed ticks") +
  labs(x = "Time (days)",
       y = "Number of late fed ticks",
       color = "Stage",
       linetype = "") +
  theme_classic() +
  scale_y_continuous(breaks = seq(0, 10000, by = 1000), limits = c(0, 10000), expand = c(0, 0)) +
  scale_x_continuous(breaks = c(15, 31, 46, 59, 75, 90, 105, 120, 135, 151, 166, 181, 196, 212, 227, 243, 258, 273, 288, 304, 319, 334, 349, 365),
                     labels = c("Jan", "", "Feb", "", "Mar", "", "Apr", "", "May", "", "Jun", "", "Jul", "", "Aug", "", 
                                "Sep", "", "Oct", "", "Nov", "", "Dec", ""),
                     expand = c(0, 0), 
                     limits = c(0, 365)) + 
  theme(plot.title = element_text(colour = "black", size = 22, face = "bold", hjust = 0.5),
        plot.margin = unit(c(0.25, 0.75, 0.25, 0.25), "cm"),
        axis.text.x = element_text(colour = "black", size = 18, hjust = 0.5),
        axis.text.y = element_text(colour = "black", size = 14),
        axis.title.x = element_text(colour = "black", size = 24, face = "bold", margin = margin(t = 12, r = 0, b = 0, l = 0)),
        axis.title.y = element_text(colour = "black", size = 24, face = "bold", margin = margin(t = 0, r = 12, b = 0, l = 0)),
        axis.line = element_line(size = 0.5),
        axis.ticks = element_line(colour = "black"),
        panel.grid.major.y = element_line(colour = "#e4e4e4", size = 0.1),
        panel.border = element_rect(fill = NA, size = 0.5)) 

ggsave(filename = "abundance_late_fed_ticks.pdf", device = "pdf", path = plot_directory,
       width = 35, height = 15, units = "cm")
abundance_plot_late_fed
dev.off()

# Create plot of not questing ticks (all life cycle stages)
abundance_plot_not_questing <- ggplot(data = abundance) +
  geom_vline(xintercept = 31,  linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 59,  linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 90,  linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 120, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 151, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 181, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 212, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 243, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 273, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 304, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 334, linetype = "solid", size = 0.1, colour = "lightgray") +
  
  geom_line(size = 1, colour = "#d7b5d8", linetype = "dotted", aes(x = tick, y = not_questing_larvae)) +
  geom_line(size = 1, colour = "#df65b0", linetype = "solid", aes(x = tick, y = not_questing_nymphs)) +
  geom_line(size = 1, colour = "#dd1c77", linetype = "dashed", aes(x = tick, y = not_questing_adults)) +
  
  ggtitle("Not questing ticks, i.e. inactive + fed (+ late fed)") +
  labs(x = "Time (days)",
       y = "Number of not questing ticks",
       color = "Stage",
       linetype = "") +
  theme_classic() +
  scale_y_continuous(breaks = seq(0, 25000, by = 5000), limits = c(0, 25000), expand = c(0, 0)) +
  scale_x_continuous(breaks = c(15, 31, 46, 59, 75, 90, 105, 120, 135, 151, 166, 181, 196, 212, 227, 243, 258, 273, 288, 304, 319, 334, 349, 365),
                     labels = c("Jan", "", "Feb", "", "Mar", "", "Apr", "", "May", "", "Jun", "", "Jul", "", "Aug", "", 
                                "Sep", "", "Oct", "", "Nov", "", "Dec", ""),
                     expand = c(0, 0), 
                     limits = c(0, 365)) + 
  theme(plot.title = element_text(colour = "black", size = 22, face = "bold", hjust = 0.5),
        plot.margin = unit(c(0.25, 0.75, 0.25, 0.25), "cm"),
        axis.text.x = element_text(colour = "black", size = 18, hjust = 0.5),
        axis.text.y = element_text(colour = "black", size = 14),
        axis.title.x = element_text(colour = "black", size = 24, face = "bold", margin = margin(t = 12, r = 0, b = 0, l = 0)),
        axis.title.y = element_text(colour = "black", size = 24, face = "bold", margin = margin(t = 0, r = 12, b = 0, l = 0)),
        axis.line = element_line(size = 0.5),
        axis.ticks = element_line(colour = "black"),
        panel.grid.major.y = element_line(colour = "#e4e4e4", size = 0.1),
        panel.border = element_rect(fill = NA, size = 0.5)) 

ggsave(filename = "abundance_not_questing_ticks.pdf", device = "pdf", path = plot_directory,
       width = 35, height = 15, units = "cm")
abundance_plot_not_questing
dev.off()

# Create plot of all life cycle stages and all activity stages
abundance_plot_all <- ggplot(data = abundance) +
  geom_vline(xintercept = 31,  linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 59,  linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 90,  linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 120, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 151, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 181, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 212, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 243, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 273, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 304, linetype = "solid", size = 0.1, colour = "lightgray") +
  geom_vline(xintercept = 334, linetype = "solid", size = 0.1, colour = "lightgray") +
  
  geom_line(size = 1, colour = "#4292c6", linetype = "solid", aes(x = tick, y = questing_nymphs)) +
  geom_line(size = 1, colour = "#6baed6", linetype = "dotted", aes(x = tick, y = questing_larvae)) +
  geom_line(size = 1, colour = "#08519c", linetype = "dashed", aes(x = tick, y = questing_adults)) +
  geom_line(size = 1, colour = "#fecc5c", linetype = "dotted", aes(x = tick, y = inactive_larvae)) +
  geom_line(size = 1, colour = "#fd8d3c", linetype = "solid", aes(x = tick, y = inactive_nymphs)) +
  geom_line(size = 1, colour = "#f03b20", linetype = "dashed", aes(x = tick, y = inactive_adults)) +
  geom_line(size = 1, colour = "#bae4b3", linetype = "dotted", aes(x = tick, y = fed_larvae)) +
  geom_line(size = 1, colour = "#74c476", linetype = "solid", aes(x = tick, y = fed_nymphs)) +
  geom_line(size = 1, colour = "#31a354", linetype = "dashed", aes(x = tick, y = fed_adults)) +
  geom_line(size = 1, colour = "#9e9ac8", linetype = "dotted", aes(x = tick, y = late_fed_larvae)) +
  geom_line(size = 1, colour = "#756bb1", linetype = "solid", aes(x = tick, y = late_fed_nymphs)) +
  
  ggtitle("Mixed life cycle stages") +
  labs(x = "Time (days)",
       y = "Number of ticks",
       color = "Stage",
       linetype = "") +
  theme_classic() +
  scale_y_continuous(breaks = seq(0, 24000, by = 2000), limits = c(0, 24000), expand = c(0, 0)) +
  scale_x_continuous(breaks = c(15, 31, 46, 59, 75, 90, 105, 120, 135, 151, 166, 181, 196, 212, 227, 243, 258, 273, 288, 304, 319, 334, 349, 365),
                     labels = c("Jan", "", "Feb", "", "Mar", "", "Apr", "", "May", "", "Jun", "", "Jul", "", "Aug", "", 
                                "Sep", "", "Oct", "", "Nov", "", "Dec", ""),
                     expand = c(0, 0), 
                     limits = c(0, 365)) + 
  theme(plot.title = element_text(colour = "black", size = 22, face = "bold", hjust = 0.5),
        plot.margin = unit(c(0.25, 0.75, 0.25, 0.25), "cm"),
        axis.text.x = element_text(colour = "black", size = 18, hjust = 0.5),
        axis.text.y = element_text(colour = "black", size = 14),
        axis.title.x = element_text(colour = "black", size = 24, face = "bold", margin = margin(t = 12, r = 0, b = 0, l = 0)),
        axis.title.y = element_text(colour = "black", size = 24, face = "bold", margin = margin(t = 0, r = 12, b = 0, l = 0)),
        axis.line = element_line(size = 0.5),
        axis.ticks = element_line(colour = "black"),
        panel.grid.major.y = element_line(colour = "#e4e4e4", size = 0.1),
        panel.border = element_rect(fill = NA, size = 0.5)) 

ggsave(filename = "abundance_mixed_stages.pdf", device = "pdf", path = plot_directory,
       width = 35, height = 15, units = "cm")
abundance_plot_all
dev.off()
