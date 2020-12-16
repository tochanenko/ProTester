import {NgModule} from "@angular/core";
import {MatCardModule} from "@angular/material/card";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {MatSelectModule} from "@angular/material/select";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatTableModule} from "@angular/material/table";
import {MatMenuModule} from "@angular/material/menu";
import {MatGridListModule} from "@angular/material/grid-list";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatBottomSheetModule} from "@angular/material/bottom-sheet";
import {MatExpansionModule} from '@angular/material/expansion';
import {MatDialogModule} from "@angular/material/dialog";
import {MatRadioModule} from "@angular/material/radio";
import {MatTabsModule} from "@angular/material/tabs";
import {MatTreeModule} from '@angular/material/tree';
import {MatChipsModule} from '@angular/material/chips';

@NgModule({
  imports: [
    MatMenuModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSelectModule,
    MatToolbarModule,
    MatTableModule,
    MatMenuModule,
    MatPaginatorModule,
    MatGridListModule,
    MatBottomSheetModule,
    MatExpansionModule,
    MatDialogModule,
    MatDialogModule,
    MatRadioModule,
    MatTabsModule,
    MatTreeModule
  ],
  exports: [
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSelectModule,
    MatToolbarModule,
    MatTableModule,
    MatMenuModule,
    MatPaginatorModule,
    MatGridListModule,
    MatBottomSheetModule,
    MatExpansionModule,
    MatDialogModule,
    MatDialogModule,
    MatRadioModule,
    MatTabsModule,
    MatTreeModule
  ]
})
export class MaterialModule {
}
