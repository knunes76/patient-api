import React from 'react';
import { cn } from '../../lib/utils';

const Table = ({ className, ...props }) => (
  <div className="w-full overflow-auto">
    <table
      className={cn("w-full caption-bottom text-sm", className)}
      {...props}
    />
  </div>
);

const TableHeader = ({ className, ...props }) => (
  <thead className={cn("[&_tr]:border-b", className)} {...props} />
);

const TableRow = ({ className, ...props }) => (
  <tr
    className={cn(
      "border-b transition-colors hover:bg-gray-50/50 data-[state=selected]:bg-gray-100",
      className
    )}
    {...props}
  />
);

const TableHead = ({ className, ...props }) => (
  <th
    className={cn(
      "h-10 px-4 text-left align-middle font-medium text-gray-500 [&:has([role=checkbox])]:pr-0 [&>[role=checkbox]]:p-0",
      className
    )}
    {...props}
  />
);

const TableCell = ({ className, ...props }) => (
  <td
    className={cn(
      "p-4 align-middle [&:has([role=checkbox])]:pr-0 [&>[role=checkbox]]:p-0",
      className
    )}
    {...props}
  />
);

const TableBody = ({ className, ...props }) => (
  <tbody
    className={cn("[&_tr:last-child]:border-0", className)}
    {...props}
  />
);

const TableFooter = ({ className, ...props }) => (
  <tfoot
    className={cn(
      "border-t bg-gray-50/50 font-medium [&>tr]:last:border-b-0",
      className
    )}
    {...props}
  />
);

const TableCaption = ({ className, ...props }) => (
  <caption
    className={cn("mt-4 text-sm text-gray-500", className)}
    {...props}
  />
);

export {
  Table,
  TableHeader,
  TableBody,
  TableFooter,
  TableRow,
  TableHead,
  TableCell,
  TableCaption,
};
export default Table;