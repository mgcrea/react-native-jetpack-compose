export type { DateInput } from "./utils/date";

/**
 * Year range configuration for the date pickers' year selector.
 */
export type YearRange = {
  start: number;
  end: number;
};

/**
 * Represents a date range with start and end dates.
 */
export type DateRange = {
  startDate: Date | null;
  endDate: Date | null;
};

/**
 * Represents a time range with start and end times.
 */
export type TimeRange = {
  startTime: Date | null;
  endTime: Date | null;
};
